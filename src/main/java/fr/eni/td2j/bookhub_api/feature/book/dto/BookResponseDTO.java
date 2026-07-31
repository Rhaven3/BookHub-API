package fr.eni.td2j.bookhub_api.feature.book.dto;

import fr.eni.td2j.bookhub_api.feature.author.dto.AuthorResponseDTO;
import fr.eni.td2j.bookhub_api.feature.category.dto.CategoryResponseDTO;
import fr.eni.td2j.bookhub_api.feature.editor.dto.EditorResponseDTO;
import fr.eni.td2j.bookhub_api.feature.image.dto.ImageResponseDTO;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

/**
 * Classe 'BookResponseDTO' en charge de
 *
 * @author jnsualu2026
 * @since 2026-07-27
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookResponseDTO {
    private Long id;
    private String title;
    private String description;
    private LocalDate publishDate;
    private String language;
    private String isbn;
    private boolean available;
    private boolean owned;

    private List<AuthorResponseDTO> authors;
    private List<CategoryResponseDTO> categories;
    private EditorResponseDTO editor;
    private List<ImageResponseDTO> images;
}