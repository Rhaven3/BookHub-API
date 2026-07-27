package fr.eni.td2j.bookhub_api.security;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.stereotype.Service;

import fr.eni.td2j.bookhub_api.exception.InvalidRefreshTokenException;
import fr.eni.td2j.bookhub_api.feature.user.User;

@Service
public class RefreshTokenService {

    public static final long REFRESH_TOKEN_DURATION_DAYS = 7;

    private final RefreshTokenRepository repo;

    public RefreshTokenService(RefreshTokenRepository repo) {
        this.repo = repo;
    }

    public RefreshToken createRefreshToken(User user) {
        RefreshToken token = RefreshToken.builder()
                .user(user)
                .token(UUID.randomUUID().toString())
                .expiryDate(LocalDateTime.now().plusDays(REFRESH_TOKEN_DURATION_DAYS))
                .revoked(false)
                .build();
        return repo.save(token);
    }

    public RefreshToken verify(String tokenValue) {
        RefreshToken token = repo.findByToken(tokenValue)
                .orElseThrow(() -> new InvalidRefreshTokenException("Refresh token invalide"));

        if (token.isRevoked() || token.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new InvalidRefreshTokenException("Refresh token invalide ou expiré");
        }

        return token;
    }

    /**
     * Rotation : l'ancien token est supprimé et remplacé par un nouveau pour le même utilisateur.
     */
    public RefreshToken rotate(RefreshToken oldToken) {
        User user = oldToken.getUser();
        repo.delete(oldToken);
        return createRefreshToken(user);
    }

    public void revoke(RefreshToken token) {
        repo.delete(token);
    }
}