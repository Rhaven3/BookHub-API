package fr.eni.td2j.bookhub_api.user.dto.request;

import fr.eni.td2j.bookhub_api.adresse.dto.request.AddressCreateDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterDTO {
    @NotBlank(message = "Le rôle est obligatoire")
    private String role;

    private String name;

    private String firstName;

    @NotBlank(message = "L'email est obligatoire")
    @Email(message = "L'email doit être valide")
    private String email;

    @NotBlank(message = "Le mot de passe est obligatoire")
    private String password;

    private String phone;

    @Valid
    private AddressCreateDTO address;
}
