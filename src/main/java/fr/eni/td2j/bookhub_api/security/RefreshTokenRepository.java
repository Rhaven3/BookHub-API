package fr.eni.td2j.bookhub_api.security;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.eni.td2j.bookhub_api.feature.user.User;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);
    void deleteByUser(User user);
}