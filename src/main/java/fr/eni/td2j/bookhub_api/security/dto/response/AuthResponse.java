package fr.eni.td2j.bookhub_api.security.dto.response;

import fr.eni.td2j.bookhub_api.feature.user.dto.response.UserResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
@AllArgsConstructor
public class AuthResponse {
    private String accessToken;
    private final UserResponseDTO user;
    private long expiresAt;

}