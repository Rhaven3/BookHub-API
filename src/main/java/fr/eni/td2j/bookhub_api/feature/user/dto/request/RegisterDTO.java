package fr.eni.td2j.bookhub_api.feature.user.dto.request;

import fr.eni.td2j.bookhub_api.feature.address.dto.request.AddressDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterDTO {

    private String lastName;

    private String firstName;

    @NotBlank(message = "L'email est obligatoire")
    @Email(message = "L'email doit être valide")
    private String email;

    @NotBlank(message = "Le mot de passe est obligatoire")
    private String password;

    private String phone;

    @Valid
    private AddressDTO address;
}
