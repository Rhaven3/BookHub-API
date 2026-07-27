package fr.eni.td2j.bookhub_api.feature.user.dto.response;

import fr.eni.td2j.bookhub_api.feature.address.dto.request.AddressDTO;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDTO {

    private Long id;
    private String name;
    private String firstName;
    private String email;
    private String role;
    private String phone;
    private String gender;
    private AddressDTO addressDTO;
}
