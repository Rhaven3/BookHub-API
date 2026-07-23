package fr.eni.td2j.bookhub_api.security;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Filtre exécuté une seule fois par requête (OncePerRequestFilter), placé avant
 * le filtre d'authentification standard de Spring Security dans SecurityConfig.
 * <p>
 * Rôle : lire le header "Authorization: Bearer <token>", vérifier sa validité,
 * et si le token est valide, authentifier automatiquement l'utilisateur pour
 * la durée de cette requête (en remplissant le SecurityContext).
 * <p>
 * Sans ce filtre, même avec un token JWT valide, Spring Security considérerait
 * chaque requête comme anonyme.
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private HttpServletRequest request;

    public JwtAuthenticationFilter(JwtService jwtService,
                                   UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        this.request = request;

        String authHeader = request.getHeader("Authorization");

        // Si l'entête Authorization est absente ou ne commence pas par "Bearer ",
        // on laisse passer la requête sans authentifier (elle sera traitée comme anonyme,
        // et bloquée plus tard par SecurityConfig si la route nécessite d'être authentifié)
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String token = authHeader.substring(7); // retire "Bearer " (7 caractères)
            String email = jwtService.extractEmail(token);

            // On n'authentifie que si un email a été extrait ET si aucune authentification
            // n'est déjà présente dans le contexte (évite de refaire le travail inutilement)
            if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                UserDetails userDetails = userDetailsService.loadUserByUsername(email);

                // Vérifie que le token correspond bien à cet utilisateur et n'est pas expiré
                if (jwtService.isTokenValid(token, userDetails)) {

                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null, // pas de mot de passe nécessaire ici, le token a déjà prouvé l'identité
                            userDetails.getAuthorities());

                    // Place l'utilisateur authentifié dans le contexte de sécurité
                    // pour toute la durée de traitement de cette requête
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }

        } catch (Exception e) {
            // Token invalide, expiré, malformé... on ne bloque pas ici :
            // la requête continuera sans authentification, et sera rejetée plus tard
            // par SecurityConfig si la route nécessite d'être authentifié.
            System.out.println("JWT ERROR: " + e.getMessage());
        }

        filterChain.doFilter(request, response);
    }
}