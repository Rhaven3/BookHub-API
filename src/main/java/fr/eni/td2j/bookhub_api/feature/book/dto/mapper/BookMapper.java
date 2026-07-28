package fr.eni.td2j.bookhub_api.feature.book.dto.mapper;

import fr.eni.td2j.bookhub_api.feature.author.dto.mapper.AuthorMapper;
import fr.eni.td2j.bookhub_api.feature.book.Book;
import fr.eni.td2j.bookhub_api.feature.book.dto.BookResponseDTO;
import fr.eni.td2j.bookhub_api.feature.category.dto.mapper.CategoryMapper;
import fr.eni.td2j.bookhub_api.feature.editor.dto.mapper.EditorMapper;
import fr.eni.td2j.bookhub_api.feature.image.dto.mapper.ImageMapper;
import org.springframework.stereotype.Component;

/**
 * Classe 'BookMapper' en charge de
 *
 * @author jnsualu2026
 * @since 2026-07-27
 */
@Component
public class BookMapper {

    public BookResponseDTO toDto(Book book) {

        return BookResponseDTO.builder()
                .id(book.getId())
                .title(book.getTitle())
                .description(book.getDescription())
                .publishDate(book.getPublishDate())
                .language(book.getLanguage())
                .isbn(book.getIsbn())
                .available(book.isAvailable())
                .owned(book.isOwned())
                .authors(
                        book.getAuthors()
                                .stream()
                                .map(AuthorMapper::toDto)
                                .toList()
                )
                .categories(
                        book.getCategories()
                                .stream()
                                .map(CategoryMapper::toDto)
                                .toList()
                )
                .editor(
                        EditorMapper.toDto(book.getEditor())
                )
                .images(
                        book.getImages()
                                .stream()
                                .map(ImageMapper::toDto)
                                .toList()
                )
                .build();
    }
}