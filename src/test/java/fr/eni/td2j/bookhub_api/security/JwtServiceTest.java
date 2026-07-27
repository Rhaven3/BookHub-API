package fr.eni.td2j.bookhub_api.security;

import fr.eni.td2j.bookhub_api.feature.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    private JwtService jwtService;
    private User user;

    // Clé de test : au moins 32 caractères pour HS256
    private static final String TEST_SECRET = "test-secret-key-1234567890-abcdefghijklmnop";

    // On initialise un USER et un JwtService via une key secrète factise
    @BeforeEach
    void setUp() {
        jwtService = new JwtService(TEST_SECRET);

        user = User.builder()
                .id(1L)
                .name("Dupont")
                .firstName("Jean")
                .email("jean@eni.fr")
                .role("USER")
                .password("hash-bidon")
                .build();
    }

    // On test si il génère bien un token
    @Test
    void testGenerateTokenNotNullOrEmpty() {
        String token = jwtService.generateToken(user);

        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    // On test si on peut bien récupérer l'email du user associé
    @Test
    void testExtractEmailFromToken() {
        String token = jwtService.generateToken(user);

        String email = jwtService.extractEmail(token);

        assertEquals("jean@eni.fr", email);
    }

    // On vérifie si le token reçu est valid , c'est à dire si il provient du user qui l'utilise
    @Test
    void testTokenIsValidForCorrectUser() {
        String token = jwtService.generateToken(user);

        UserDetails userDetails = new CustomUserDetails(user);

        assertTrue(jwtService.isTokenValid(token, userDetails));
    }

    // On vérifie que le token n'est pas valide si on l'utilise avec un autre user
    @Test
    void testTokenIsInvalidForDifferentUser() {
        String token = jwtService.generateToken(user);

        User autreUser = User.builder()
                .id(2L)
                .email("autre@eni.fr")
                .role("USER")
                .password("hash-bidon")
                .build();

        UserDetails userDetails = new CustomUserDetails(autreUser);

        assertFalse(jwtService.isTokenValid(token, userDetails));
    }
}