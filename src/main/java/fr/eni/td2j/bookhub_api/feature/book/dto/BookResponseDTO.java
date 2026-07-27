package fr.eni.td2j.bookhub_api.feature.book.dto;

import fr.eni.td2j.bookhub_api.feature.image.dto.ImageResponseDTO;
import lombok.*;

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
    private String isbn;
    private List<ImageResponseDTO> images;
}
