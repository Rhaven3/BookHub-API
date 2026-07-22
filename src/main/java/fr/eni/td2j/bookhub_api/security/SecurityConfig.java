package fr.eni.td2j.bookhub_api.security;

import java.util.List;

import fr.eni.td2j.bookhub_api.services.UserDetailsCustomService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
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
        config.setAllowedOrigins(List.of("http://localhost:5173"));
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
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable()) // pas besoin de CSRF en API stateless (pas de cookies de session)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // pas de session HTTP, tout passe par le JWT
                .authorizeHttpRequests(auth -> auth
                        // --- Routes publiques (à adapter à TES routes BookHub) ---
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        .requestMatchers(org.springframework.http.HttpMethod.GET, "/api/livres/**").permitAll() // ex: consultation libre du catalogue
                        .requestMatchers(org.springframework.http.HttpMethod.OPTIONS, "/**").permitAll() // requêtes préflight CORS

                        // --- Routes protégées (exemples à adapter) ---
                        .requestMatchers("/api/auth/me").authenticated()
                        .requestMatchers("/api/utilisateurs/**").authenticated()
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")

                        // --- Tout le reste nécessite d'être authentifié ---
                        .anyRequest().authenticated())
                // .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class) // insère notre filtre JWT avant celui de Spring
                //.httpBasic(AbstractHttpConfigurer::disable); // on désactive l'auth HTTP Basic (on n'utilise que le JWT)
                .httpBasic(basic -> {});
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
}