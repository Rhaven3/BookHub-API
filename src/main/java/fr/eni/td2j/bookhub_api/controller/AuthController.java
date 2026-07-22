package fr.eni.td2j.bookhub_api.controller;

import fr.eni.td2j.bookhub_api.dto.request.LoginDTO;
import fr.eni.td2j.bookhub_api.entity.User;
import fr.eni.td2j.bookhub_api.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginDTO dto) {

        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new BadCredentialsException("Email ou mot de passe incorrect"));

        if (!passwordEncoder.matches(dto.getPassword(), user.getMotDePasse())) {
            throw new BadCredentialsException("Email ou mot de passe incorrect");
        }

        // Pour l'instant, pas de token : on confirme juste que l'authentification fonctionne
        return ResponseEntity.ok("Connexion réussie pour " + user.getEmail());
    }
}
