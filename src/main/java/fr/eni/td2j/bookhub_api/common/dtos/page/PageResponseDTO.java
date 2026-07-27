package fr.eni.td2j.bookhub_api.common.dtos.page;

import lombok.*;

import java.util.List;

/**
 * Classe 'PageResponseDTO' en charge de
 *
 * @author jnsualu2026
 * @since 2026-07-27
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageResponseDTO<T> {

    private List<T> content;
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;
    private boolean first;
    private boolean last;
    private boolean empty;
}