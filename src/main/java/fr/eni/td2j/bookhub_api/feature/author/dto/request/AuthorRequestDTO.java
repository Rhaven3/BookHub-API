package fr.eni.td2j.bookhub_api.feature.author.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthorRequestDTO {

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;
}