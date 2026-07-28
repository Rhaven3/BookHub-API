package fr.eni.td2j.bookhub_api.feature.editor.dto.mapper;

import fr.eni.td2j.bookhub_api.feature.editor.Editor;
import fr.eni.td2j.bookhub_api.feature.editor.dto.EditorResponseDTO;

/**
 * Classe 'EditorMapper' en charge de
 *
 * @author jnsualu2026
 * @since 2026-07-28
 */
public class EditorMapper {
    public static EditorResponseDTO toDto(Editor editor) {
        if (editor == null) {
            return null;
        }

        return EditorResponseDTO.builder()
                .id(editor.getId())
                .name(editor.getName())
                .build();
    }
}
