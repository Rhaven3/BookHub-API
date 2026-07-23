package fr.eni.td2j.bookhub_api.security;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import fr.eni.td2j.bookhub_api.user.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

/**
 * Service responsable de la génération et de la validation des tokens JWT.
 * <p>
 * Le token contient l'email de l'utilisateur (subject), son rôle et son id (claims),
 * une date d'émission et une date d'expiration. Il est signé avec une clé secrète
 * (HMAC-SHA256) afin de garantir qu'il n'a pas été modifié par le client.
 */
@Service
public class JwtService {

    /** Durée de validité du token : 15 minutes. */
    public static final long ACCESS_TOKEN_DURATION = 1000L * 60 * 15;

    /** Clé secrète utilisée pour signer et vérifier les tokens. */
    private final SecretKey key;

    /**
     * @param secret clé secrète brute, injectée depuis application.properties (jwt.secret)
     */
    public JwtService(@Value("${jwt.secret}") String secret) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Génère un token JWT signé pour l'utilisateur donné.
     *
     * @param user l'utilisateur authentifié
     * @return le token JWT sous forme de chaîne compacte
     */
    public String generateToken(User user) {
        return Jwts.builder()
                .subject(user.getEmail())
                .claim("role", user.getRole())
                .claim("userId", user.getId())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_DURATION))
                .signWith(key)
                .compact();
    }

    /**
     * Extrait l'email (subject) contenu dans le token.
     *
     * @param token le token JWT reçu du client
     * @return l'email de l'utilisateur
     */
    public String extractEmail(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    /**
     * Vérifie que le token est valide : email correspondant à l'utilisateur attendu,
     * et token non expiré.
     *
     * @param token       le token JWT reçu
     * @param userDetails l'utilisateur chargé depuis la base (via UserDetailsCustomService)
     * @return true si le token est valide
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String email = extractEmail(token);
        return email.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    /**
     * Vérifie si le token a dépassé sa date d'expiration.
     */
    private boolean isTokenExpired(String token) {
        Date expiration = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getExpiration();

        return expiration.before(new Date());
    }
}