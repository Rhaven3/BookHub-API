package fr.eni.td2j.bookhub_api.security;

import fr.eni.td2j.bookhub_api.user.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JwtAuthenticationFilterTest {

    @Mock
    private JwtService jwtService;

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @BeforeEach
    void setUp() {
        // TODO : instancier jwtAuthenticationFilter avec les mocks
        jwtAuthenticationFilter = new JwtAuthenticationFilter(jwtService, userDetailsService);
        SecurityContextHolder.clearContext();
    }

    // On vérifie que si le header Authorization est absent, la requête continue sans authentification
    // On doit passer dans : filterChain.doFilter(request, response); ligne 53 de JwtAuthenticationFilter
    @Test
    void testNoAuthorizationHeader_ShouldContinueFilterChainWithoutAuthentication() throws Exception{
        when(request.getHeader("Authorization")).thenReturn(null);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);
        verify(filterChain).doFilter(request, response);
    }

    // On vérifie que si le header Authorization existe mais ne commence pas par "Bearer ",
    // la requête continue sans authentification
    // Pareil que le test précédent
    @Test
    void testAuthorizationHeaderWithoutBearerPrefix_ShouldContinueFilterChainWithoutAuthentication() throws Exception {
        when(request.getHeader("Authorization")).thenReturn("Basic abc123");

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
    }

    //  On teste la validité du token pour vérifier qu'il set bien un contexte
    // En créant un UsernamePasswordAuthenticationToken
    @Test
    void testValidToken_ShouldSetAuthenticationInSecurityContext() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setEmail("user@example.com");
        user.setName("user");
        user.setFirstName("test");
        CustomUserDetails userDetails = new CustomUserDetails(user);


        when(request.getHeader("Authorization")).thenReturn("Bearer validToken");
        when(jwtService.isTokenValid("validToken", userDetails)).thenReturn(true);
        when(jwtService.extractEmail("validToken")).thenReturn("user@example.com");
        when(userDetailsService.loadUserByUsername("user@example.com")).thenReturn(userDetails);
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        verify(jwtService).extractEmail("validToken");
        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
    }

    // On teste le cas où isTokenValid renvoie false :
    // on vérifie qu'aucun contexte de sécurité n'est créé
    @Test
    void testInvalidToken_ShouldNotSetAuthenticationInSecurityContext() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setEmail("user@example.com");
        user.setName("user");
        user.setFirstName("test");
        CustomUserDetails userDetails = new CustomUserDetails(user);


        when(request.getHeader("Authorization")).thenReturn("Bearer inValidToken");
        when(jwtService.isTokenValid("inValidToken", userDetails)).thenReturn(false);
        when(jwtService.extractEmail("inValidToken")).thenReturn("user@example.com");
        when(userDetailsService.loadUserByUsername("user@example.com")).thenReturn(userDetails);
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        verify(jwtService).extractEmail("inValidToken");
        verify(jwtService).isTokenValid("inValidToken", userDetails);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    // On simule une exception levée par jwtService (token expiré/malformé) : on vérifie qu'elle est bien absorbée
    // par le try/catch du filtre, sans remonter, et que la requête continue son chemin normalement
    @Test
    void testExpiredOrMalformedToken_ShouldNotThrowAndContinueFilterChain() throws Exception {
        when(request.getHeader("Authorization")).thenReturn("Bearer malformedToken");
        when(jwtService.extractEmail("malformedToken")).thenThrow(new RuntimeException("Token invalide"));

        // On vérifie qu'aucune exception ne remonte jusqu'ici
        assertDoesNotThrow(() ->
                jwtAuthenticationFilter.doFilterInternal(request, response, filterChain)
        );

        verify(filterChain).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }
}