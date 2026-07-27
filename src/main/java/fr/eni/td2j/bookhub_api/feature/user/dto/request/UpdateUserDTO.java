package fr.eni.td2j.bookhub_api.feature.user.dto.request;

import fr.eni.td2j.bookhub_api.feature.adresse.dto.request.AddressDTO;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUserDTO {
    private String name;
    private String firstName;
    private String phone;

    @Valid
    private AddressDTO address;
}
