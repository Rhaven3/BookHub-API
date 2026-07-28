package fr.eni.td2j.bookhub_api.security;

import fr.eni.td2j.bookhub_api.common.ApiResponse;
import fr.eni.td2j.bookhub_api.exception.InvalidRefreshTokenException;
import fr.eni.td2j.bookhub_api.feature.user.User;
import fr.eni.td2j.bookhub_api.feature.user.UserMapper;
import fr.eni.td2j.bookhub_api.feature.user.UserRepository;
import fr.eni.td2j.bookhub_api.feature.user.UserService;
import fr.eni.td2j.bookhub_api.feature.user.dto.request.LoginDTO;
import fr.eni.td2j.bookhub_api.feature.user.dto.request.RegisterDTO;
import fr.eni.td2j.bookhub_api.feature.user.dto.response.UserResponseDTO;
import fr.eni.td2j.bookhub_api.security.dto.response.AuthResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final String REFRESH_COOKIE_NAME = "refreshToken";
    private static final long REFRESH_COOKIE_MAX_AGE = RefreshTokenService.REFRESH_TOKEN_DURATION_DAYS * 24 * 60 * 60;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserService userService;
    private final RefreshTokenService refreshTokenService;
    private final UserMapper userMapper;

    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtService jwtService, UserService userService, RefreshTokenService refreshTokenService, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userService = userService;
        this.refreshTokenService = refreshTokenService;
        this.userMapper = userMapper;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginDTO dto, HttpServletResponse response) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword())
        );

        User user = userRepository.findByEmail(dto.getEmail()).orElseThrow();
        UserResponseDTO userResponseDTO = userMapper.toDto(user);

        String accessToken = jwtService.generateToken(user);
        long expiresAt = System.currentTimeMillis() + JwtService.ACCESS_TOKEN_DURATION;

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);
        response.addHeader(HttpHeaders.SET_COOKIE, buildRefreshCookie(refreshToken.getToken(), REFRESH_COOKIE_MAX_AGE).toString());

        AuthResponse authResponse = new AuthResponse(accessToken, userResponseDTO, expiresAt);
        return ResponseEntity.ok(authResponse);
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<?>> register(@RequestBody RegisterDTO dto) {
        userService.register(dto);
        ApiResponse<?> response = ApiResponse.success(null,"Compte créé avec succès");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(HttpServletRequest request, HttpServletResponse response) {

        String cookieValue = extractRefreshCookie(request)
                .orElseThrow(() -> new InvalidRefreshTokenException("Refresh token manquant"));

        RefreshToken verified = refreshTokenService.verify(cookieValue);
        RefreshToken rotated = refreshTokenService.rotate(verified);

        User user = rotated.getUser();
        UserResponseDTO userResponseDTO = userMapper.toDto(user);
        String accessToken = jwtService.generateToken(user);
        long expiresAt = System.currentTimeMillis() + JwtService.ACCESS_TOKEN_DURATION;

        response.addHeader(HttpHeaders.SET_COOKIE, buildRefreshCookie(rotated.getToken(), REFRESH_COOKIE_MAX_AGE).toString());

        AuthResponse authResponse = new AuthResponse(accessToken, userResponseDTO, expiresAt);
        return ResponseEntity.ok(authResponse);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {

        extractRefreshCookie(request).ifPresent(cookieValue -> {
            try {
                refreshTokenService.revoke(refreshTokenService.verify(cookieValue));
            } catch (InvalidRefreshTokenException ignored) {
                // token déjà invalide/absent en base : rien à révoquer
            }
        });

        response.addHeader(HttpHeaders.SET_COOKIE, buildRefreshCookie("", 0).toString());

        return ResponseEntity.ok().build();
    }

    private java.util.Optional<String> extractRefreshCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return java.util.Optional.empty();
        }
        return Arrays.stream(cookies)
                .filter(c -> REFRESH_COOKIE_NAME.equals(c.getName()))
                .findFirst()
                .map(Cookie::getValue);
    }

    private ResponseCookie buildRefreshCookie(String value, long maxAgeSeconds) {
        return ResponseCookie.from(REFRESH_COOKIE_NAME, value)
                .httpOnly(true)
                .secure(false) // TODO: passer à true en prod (HTTPS)
                .path("/api/auth")
                .sameSite("Lax")
                .maxAge(maxAgeSeconds)
                .build();
    }
}