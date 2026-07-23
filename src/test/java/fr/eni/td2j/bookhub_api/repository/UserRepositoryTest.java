package fr.eni.td2j.bookhub_api.repository;

import fr.eni.td2j.bookhub_api.user.User;
import fr.eni.td2j.bookhub_api.user.UserDetailsCustomService;
import fr.eni.td2j.bookhub_api.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserRepositoryTest {

    @Mock
    private UserRepository userRepository;

    private UserDetailsCustomService userDetailsCustomService;

    private User user;

    @BeforeEach
    void setUp() {
        userDetailsCustomService = new UserDetailsCustomService(userRepository);
        user = User.builder()
                .id(1l)
                .name("Dupont")
                .firstName("Jean")
                .email("jean@eni.fr")
                .phone("0600000000")
                .role("USER")
                .password("$2a$10$wYQiPnp/4zOC89RZ96a4W.B6yYFx.ZW90QI0gD5ng7klO1jle2oQO") // Julien en mdp
                .build();
    }

    @Test
    void testGetUserByEmail() {
      when(userRepository.findByEmail("jean@eni.fr")).thenReturn(Optional.of(user));
      UserDetails result = userDetailsCustomService.loadUserByUsername("jean@eni.fr");

      assertEquals("jean@eni.fr", result.getUsername());
      assertEquals("$2a$10$wYQiPnp/4zOC89RZ96a4W.B6yYFx.ZW90QI0gD5ng7klO1jle2oQO", result.getPassword());
      assertEquals(1, result.getAuthorities().size());
    }
    @Test
    void testLoginWithValidCredentials() {
        when(userRepository.findByEmail("jean@eni.fr")).thenReturn(Optional.of(user));

        UserDetails result = userDetailsCustomService.loadUserByUsername("jean@eni.fr");

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        // On vérifie que le mot de passe en clair correspond bien au hash stocké
        boolean passwordMatches = passwordEncoder.matches("julien", result.getPassword());

        assertTrue(passwordMatches);
    }
    @Test
    void testLoginWithInvalidCredentials() {
        when(userRepository.findByEmail("jean@eni.fr")).thenReturn(Optional.of(user));

        UserDetails result = userDetailsCustomService.loadUserByUsername("jean@eni.fr");

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        boolean passwordMatches = passwordEncoder.matches("mauvais_mot_de_passe", result.getPassword());

        assertFalse(passwordMatches);
    }
}
