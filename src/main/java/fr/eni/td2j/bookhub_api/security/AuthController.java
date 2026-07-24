package fr.eni.td2j.bookhub_api.security;

import fr.eni.td2j.bookhub_api.user.User;
import fr.eni.td2j.bookhub_api.user.UserRepository;
import fr.eni.td2j.bookhub_api.user.dto.request.LoginDTO;
import fr.eni.td2j.bookhub_api.security.dto.response.AuthResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }
    @PostMapping("/test")
    public void test (){
        System.out.println("Le user est connecté");
    }
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginDTO dto) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword())
        );

        User user = userRepository.findByEmail(dto.getEmail()).orElseThrow();

        String token = jwtService.generateToken(user);
        long expiresAt = System.currentTimeMillis() + JwtService.ACCESS_TOKEN_DURATION;

        AuthResponse response = new AuthResponse(token, user.getEmail(), user.getRole(), expiresAt);
        return ResponseEntity.ok(response);
    }
//    @PostMapping("/register")
//    public ResponseEntity<> register(@RequestBody RegisterDTO dto) {
//
//        ApiResponse<?> response = authService.register(dto);
//
//        return ResponseEntity.ok(response);
//    }
}
