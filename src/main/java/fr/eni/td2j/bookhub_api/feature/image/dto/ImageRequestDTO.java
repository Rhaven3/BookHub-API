package fr.eni.td2j.bookhub_api.feature.image.dto;

import lombok.*;

/**
 * Classe 'ImageRequestDTO' en charge de
 *
 * @author jnsualu2026
 * @since 2026-07-27
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImageRequestDTO {
    private String name;
    private String path;
}
