package fr.eni.td2j.bookhub_api.feature.image.dto.mapper;

import fr.eni.td2j.bookhub_api.feature.image.Image;
import fr.eni.td2j.bookhub_api.feature.image.dto.ImageResponseDTO;
import org.springframework.stereotype.Component;

/**
 * Classe 'ImageMapper' en charge de
 *
 * @author jnsualu2026
 * @since 2026-07-27
 */
@Component
public class ImageMapper {

    public static ImageResponseDTO toDto(Image image) {
        return ImageResponseDTO.builder()
                .id(image.getId())
                .name(image.getName())
                .path(image.getPath())
                .build();
    }
}
