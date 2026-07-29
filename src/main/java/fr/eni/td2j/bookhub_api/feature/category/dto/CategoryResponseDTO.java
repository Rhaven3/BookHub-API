package fr.eni.td2j.bookhub_api.feature.category.dto;

import lombok.*;

/**
 * Classe 'CategoryResponseDTO' en charge de
 *
 * @author jnsualu2026
 * @since 2026-07-28
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryResponseDTO {
    private Long id;
    private String name;
}
