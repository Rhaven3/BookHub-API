package fr.eni.td2j.bookhub_api.feature.category.dto.mapper;

import fr.eni.td2j.bookhub_api.feature.category.Category;
import fr.eni.td2j.bookhub_api.feature.category.dto.CategoryResponseDTO;
import org.springframework.stereotype.Component;

/**
 * Classe 'CategoryMapper' en charge de convertir
 * une entité Category en CategoryResponseDTO.
 *
 * @author jnsualu2026
 * @since 2026-07-28
 */
@Component
public class CategoryMapper {

    public static CategoryResponseDTO toDto(Category category) {
        if (category == null) {
            return null;
        }

        return CategoryResponseDTO.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }
}