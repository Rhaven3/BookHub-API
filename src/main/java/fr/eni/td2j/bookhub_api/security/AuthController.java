package fr.eni.td2j.bookhub_api.security;

import fr.eni.td2j.bookhub_api.common.ApiResponse;
import fr.eni.td2j.bookhub_api.feature.user.User;
import fr.eni.td2j.bookhub_api.feature.user.UserRepository;
import fr.eni.td2j.bookhub_api.feature.user.UserService;
import fr.eni.td2j.bookhub_api.feature.user.dto.request.LoginDTO;
import fr.eni.td2j.bookhub_api.feature.user.dto.request.RegisterDTO;
import fr.eni.td2j.bookhub_api.security.dto.response.AuthResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
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
    private final UserService userService;

    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtService jwtService, UserService userService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userService = userService;
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
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<?>> register(@RequestBody RegisterDTO dto) {
        userService.register(dto);
        ApiResponse<?> response = ApiResponse.success("Compte créé avec succès", null);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {

        ResponseCookie cookie = ResponseCookie.from("refreshToken", "")
                .httpOnly(true)
                .secure(false)
                .path("/api/auth")
                .maxAge(0)
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        return ResponseEntity.ok().build();
    }
}
