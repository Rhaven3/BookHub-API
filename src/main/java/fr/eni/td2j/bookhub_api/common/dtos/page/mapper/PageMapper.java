package fr.eni.td2j.bookhub_api.common.dtos.page.mapper;

import fr.eni.td2j.bookhub_api.common.dtos.page.PageResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

/**
 * Classe 'PageMapper' en charge de
 *
 * @author jnsualu2026
 * @since 2026-07-27
 */
@Component
public class PageMapper {

    public static <T> PageResponseDTO<T> toDto(Page<T> page) {
        return PageResponseDTO.<T>builder()
                .content(page.getContent())
                .page(page.getNumber())
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .first(page.isFirst())
                .last(page.isLast())
                .empty(page.isEmpty())
                .build();
    }
}