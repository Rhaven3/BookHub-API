package fr.eni.td2j.bookhub_api.feature.book.dto;

import fr.eni.td2j.bookhub_api.feature.author.dto.request.AuthorRequestDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

/**
 * Classe 'BookRequestDTO' en charge de
 *
 * @author jnsualu2026
 * @since 2026-07-28
 */
@Getter
@Setter
public class BookRequestDTO {

    @NotBlank
    private String title;

    @Size(max = 2000)
    private String description;

    @NotNull
    @PastOrPresent
    private LocalDate publishDate;

    @NotBlank
    private String language;

    @NotBlank
    @Pattern(regexp = "^(97(8|9))?\\d{9}(\\d|X)$")
    private String isbn;

    @NotNull
    private Long editorId;

    private List<Long> authorIds;

    @Valid
    private List<AuthorRequestDTO> newAuthors;

    @NotEmpty(message = "Le livre doit avoir au moins une catégorie")
    private List<Long> categoryIds;

    private List<Long> keepImageIds;
    private List<String> newImageNames;
}