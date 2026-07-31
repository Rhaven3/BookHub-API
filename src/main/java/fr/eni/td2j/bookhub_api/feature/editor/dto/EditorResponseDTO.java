package fr.eni.td2j.bookhub_api.feature.editor.dto;

import lombok.*;

/**
 * Classe 'EditorResponseDTO' en charge de
 *
 * @author jnsualu2026
 * @since 2026-07-28
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EditorResponseDTO {
    private Long id;
    private String name;
}
