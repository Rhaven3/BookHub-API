package fr.eni.td2j.bookhub_api.feature.book.dto.mapper;

import fr.eni.td2j.bookhub_api.feature.book.Book;
import fr.eni.td2j.bookhub_api.feature.book.dto.BookResponseDTO;
import fr.eni.td2j.bookhub_api.feature.image.dto.mapper.ImageMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Classe 'BookMapper' en charge de
 *
 * @author jnsualu2026
 * @since 2026-07-27
 */
@Component
@RequiredArgsConstructor
public class BookMapper {

    private static final ImageMapper imageMapper = new ImageMapper();

    public BookResponseDTO toDto(Book book) {
        return BookResponseDTO.builder()
                .id(book.getId())
                .title(book.getTitle())
                .isbn(book.getIsbn())
                .images(
                        book.getImages() == null
                                ? List.of()
                                : book.getImages()
                                .stream()
                                .map(imageMapper::toDto)
                                .toList()
                )
                .build();
    }
}
