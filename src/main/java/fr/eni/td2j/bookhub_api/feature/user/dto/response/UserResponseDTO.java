package fr.eni.td2j.bookhub_api.feature.user.dto.response;

import fr.eni.td2j.bookhub_api.feature.adresse.dto.request.AddressDTO;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDTO {

    private String lastName;
    private String firstName;
    private String email;
    private String role;
    private String phone;
    private String gender;
    private AddressDTO addressDTO;
}
