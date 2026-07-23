package fr.eni.td2j.bookhub_api.user.dto.response;

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
    private String email;
    private String role;
    private long expiresAt;

}