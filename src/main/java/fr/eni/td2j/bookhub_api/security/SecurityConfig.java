package fr.eni.td2j.bookhub_api.security;

import java.util.List;

import fr.eni.td2j.bookhub_api.feature.user.Role;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.authorization.DefaultAuthorizationManagerFactory;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/**
 * Configuration centrale de Spring Security pour l'API.
 * <p>
 * Définit : la politique CORS, la chaîne de filtres de sécurité (routes publiques/privées),
 * l'encodeur de mot de passe, et les beans nécessaires à l'authentification par JWT.
 */
@Configuration
@EnableMethodSecurity // active @PreAuthorize / @Secured sur les méthodes des contrôleurs/services
public class SecurityConfig {

    /**
     * Configuration CORS : autorise le frontend (sur un autre port/domaine) à appeler l'API.
     * ⚠️ À adapter avec l'URL réelle de ton frontend (ex: Vite = 5173, React CRA = 3000...).
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration config = new CorsConfiguration();

        // TODO : remplace par l'URL de TON frontend BookHub
        config.setAllowedOrigins(List.of("http://localhost:4200"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true); // nécessaire si tu envoies des cookies/credentials

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;
    }

    /**
     * Chaîne de filtres de sécurité : définit quelles routes sont publiques,
     * lesquelles nécessitent une authentification, et branche le filtre JWT.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthenticationFilter jwtAuthenticationFilter,
            CustomAuthenticationEntryPoint authenticationEntryPoint, CustomAccessDeniedHandler accessDeniedHandler) throws Exception {

        http
                .csrf(csrf -> csrf.disable()) // pas besoin de CSRF en API stateless (pas de cookies de session)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // pas de session HTTP, tout passe par le JWT
                .authorizeHttpRequests(auth -> auth
                        // --- Routes publiques (à adapter à TES routes BookHub) ---
                        .requestMatchers("/api/auth/login").permitAll()
                        .requestMatchers("/api/auth/register").permitAll()
                        // /refresh et /logout s'appuient sur le cookie refreshToken, pas sur l'access token Bearer
                        .requestMatchers("/api/auth/refresh").permitAll()
                        .requestMatchers("/api/auth/logout").permitAll()
                        .requestMatchers("/ws/**").permitAll() // l'auth se fait ensuite au niveau STOMP CONNECT

                        // Les Routes nécessitant d'être un Bibliotécaire
                        .requestMatchers("/api/loans/*/return").hasAnyRole(Role.LIBRARIAN.name(), Role.ADMIN.name())
                        .requestMatchers("/api/loans/").hasAnyRole(Role.LIBRARIAN.name(), Role.ADMIN.name())
                        // --- Tout le reste nécessite d'être authentifié ---
                        .anyRequest().authenticated())
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(authenticationEntryPoint)
                        .accessDeniedHandler(accessDeniedHandler))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class) // insère notre filtre JWT avant celui de Spring
                .httpBasic(AbstractHttpConfigurer::disable); // on désactive l'auth HTTP Basic (on n'utilise que le JWT)
        return http.build();
    }

    /**
     * Encodeur de mot de passe utilisé à l'inscription (encode) et à la connexion (matches).
     * BCrypt = algorithme de hachage à sens unique, avec salage automatique.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Fournit à Spring Security la logique pour vérifier un couple email/mot de passe :
     * - où aller chercher l'utilisateur (UserDetailsCustomService, qu'on a créé plus tôt)
     * - comment vérifier le mot de passe (le PasswordEncoder défini juste au-dessus)
     * <p>
     * C'est ce provider que l'AuthenticationManager (bean suivant) utilisera en interne
     * quand on appellera authenticationManager.authenticate(...) dans le contrôleur de login.
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider(UserDetailsCustomService userDetailsCustomService) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsCustomService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    /**
     * Expose l'AuthenticationManager en tant que bean Spring, pour pouvoir l'injecter
     * directement dans le contrôleur d'authentification (AuthController).
     * <p>
     * C'est cet objet qui sera appelé via authenticationManager.authenticate(...)
     * pour vérifier l'email + mot de passe envoyés au login.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /*
     *
     * Hiérarchie des rôles
     *
     */

    /**
     * Définit une hiérarchie entre les rôles de l'application.
     * <p>
     * Ici : un utilisateur ayant le rôle ADMIN possède automatiquement
     * tous les droits associés au rôle USER (ADMIN "implique" USER).
     * <p>
     * Concrètement, ça évite d'avoir à écrire {@code hasAnyRole("ADMIN", "USER")}
     * partout dans le code : un simple {@code hasRole("USER")} suffira,
     * et sera automatiquement validé aussi bien pour un USER que pour un ADMIN.
     * <p>
     * {@code withDefaultRolePrefix()} gère automatiquement l'ajout du préfixe
     * "ROLE_" attendu par Spring Security (pas besoin de l'écrire soi-même ici).
     */
    @Bean
    public RoleHierarchy roleHierarchy() {
        return RoleHierarchyImpl.withDefaultRolePrefix()
                .role("ADMIN").implies("USER")
                .build();
    }

    /**
     * Branche la hiérarchie de rôles définie ci-dessus sur la sécurité au niveau
     * des méthodes (celle utilisée par les annotations {@code @PreAuthorize}, {@code @PostAuthorize}...).
     * <p>
     * Sans ce bean, la {@link RoleHierarchy} ne serait prise en compte que pour
     * la sécurité au niveau des routes HTTP (dans {@code securityFilterChain}),
     * mais pas pour les annotations posées directement sur les méthodes des contrôleurs/services.
     * <p>
     * Fonctionnement :
     * 1. On crée un {@link DefaultAuthorizationManagerFactory}, responsable de construire
     *    la logique d'autorisation (ex: vérifier si l'utilisateur a bien le rôle demandé).
     * 2. On lui injecte notre {@link RoleHierarchy}, pour qu'il sache qu'ADMIN implique USER.
     * 3. On relie cette factory à notre {@link DefaultMethodSecurityExpressionHandler},
     *    qui est la classe utilisée en interne par Spring Security pour évaluer les
     *    expressions comme {@code hasRole('USER')} dans les annotations {@code @PreAuthorize}.
     * <p>
     * Le type générique {@code <MethodInvocation>} indique que cette factory est
     * spécifiquement dédiée au contexte des appels de méthode (par opposition aux
     * requêtes HTTP, qui utiliseraient un autre type générique).
     * <p>
     * Ce bean doit être {@code static} : Spring Security le construit très tôt dans
     * le cycle de démarrage de l'application, avant que le contexte Spring complet
     * (avec tous les beans "normaux") ne soit disponible.
     */
    @Bean
    static MethodSecurityExpressionHandler methodSecurityExpressionHandler(RoleHierarchy roleHierarchy) {
        DefaultMethodSecurityExpressionHandler expressionHandler = new DefaultMethodSecurityExpressionHandler();

        DefaultAuthorizationManagerFactory<MethodInvocation> factory = new DefaultAuthorizationManagerFactory<>();
        factory.setRoleHierarchy(roleHierarchy);

        expressionHandler.setAuthorizationManagerFactory(factory);
        return expressionHandler;
    }
}