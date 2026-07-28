package fr.eni.td2j.bookhub_api.feature.user.dto.request;

import fr.eni.td2j.bookhub_api.feature.address.dto.request.AddressDTO;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUserDTO {
    private String lastName;
    private String firstName;
    private String phone;
    private String email;

    @Valid
    private AddressDTO address;


}
