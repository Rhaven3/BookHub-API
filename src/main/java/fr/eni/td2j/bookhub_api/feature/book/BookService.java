package fr.eni.td2j.bookhub_api.feature.book;

import fr.eni.td2j.bookhub_api.exception.BadRequestException;
import fr.eni.td2j.bookhub_api.exception.NotFoundException;
import fr.eni.td2j.bookhub_api.feature.author.Author;
import fr.eni.td2j.bookhub_api.feature.author.AuthorRepository;
import fr.eni.td2j.bookhub_api.feature.book.dto.BookRequestDTO;
import fr.eni.td2j.bookhub_api.feature.book.dto.BookResponseDTO;
import fr.eni.td2j.bookhub_api.feature.book.dto.mapper.BookMapper;
import fr.eni.td2j.bookhub_api.feature.category.Category;
import fr.eni.td2j.bookhub_api.feature.category.CategoryRepository;
import fr.eni.td2j.bookhub_api.feature.editor.Editor;
import fr.eni.td2j.bookhub_api.feature.editor.EditorRepository;
import fr.eni.td2j.bookhub_api.feature.image.Image;
import fr.eni.td2j.bookhub_api.feature.image.ImageRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final CategoryRepository categoryRepository;
    private final EditorRepository editorRepository;
    private final ImageRepository imageRepository;
    private final BookMapper bookMapper;

    public Page<BookResponseDTO> findAll(Pageable pageable) {
         Page<Book> books = bookRepository.findAll(pageable);
         return books.map(bookMapper::toDto);
    }

    public BookResponseDTO findById(Long id) {

        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Livre introuvable."));

        return bookMapper.toDto(book);
    }

    public BookResponseDTO create(BookRequestDTO requestDTO) {

        Editor editor = getEditor(requestDTO.getEditorId());

        List<Author> authors = getAuthors(requestDTO.getAuthorIds());

        List<Category> categories = getCategories(requestDTO.getCategoryIds());

        List<Image> images = getImages(requestDTO.getImageIds());

        Book book = Book.builder()
                .title(requestDTO.getTitle())
                .description(requestDTO.getDescription())
                .publishDate(requestDTO.getPublishDate())
                .language(requestDTO.getLanguage())
                .isbn(requestDTO.getIsbn())
                .available(true)
                .owned(true)
                .editor(editor)
                .authors(authors)
                .categories(categories)
                .images(images)
                .build();

        return bookMapper.toDto(bookRepository.save(book));
    }

    private List<Category> getCategories(List<Long> categoryIds) {

        if(categoryIds == null || categoryIds.isEmpty()){
            return List.of();
        }

        List<Category> categories = categoryRepository.findAllById(categoryIds);

        if(categories.size() != categoryIds.size()){
            throw new NotFoundException("Une ou plusieurs catégories sont introuvables.");
        }

        return categories;
    }

    private List<Image> getImages(List<Long> imageIds) {

        if(imageIds == null || imageIds.isEmpty()){
            return List.of();
        }

        List<Image> images = imageRepository.findAllById(imageIds);

        if (images.size() != imageIds.size()) {
            throw new NotFoundException("Une ou plusieurs images sont introuvables.");
        }
        return images;
    }

    private List<Author> getAuthors(List<Long> authorIds) {

        if(authorIds == null || authorIds.isEmpty()){
            return List.of();
        }

        List<Author> authors = authorRepository.findAllById(authorIds);

        if (authors.size() != authorIds.size()) {
            throw new NotFoundException("Un ou plusieurs auteurs sont introuvables.");
        }
        return authors;
    }

    private Editor getEditor(Long editorId) {
        return editorRepository.findById(editorId)
                .orElseThrow(() -> new NotFoundException("Éditeur introuvable."));
    }

    @Transactional
    public BookResponseDTO update(Long id, BookRequestDTO requestBook) {

        Book existingBook = bookRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Livre introuvable."));

        Editor editor = getEditor(requestBook.getEditorId());

        List<Author> authors = getAuthors(requestBook.getAuthorIds());

        List<Category> categories = getCategories(requestBook.getCategoryIds());

        List<Image> images = getImages(requestBook.getImageIds());

        existingBook.setTitle(requestBook.getTitle());
        existingBook.setDescription(requestBook.getDescription());
        existingBook.setPublishDate(requestBook.getPublishDate());
        existingBook.setLanguage(requestBook.getLanguage());
        existingBook.setIsbn(requestBook.getIsbn());

        existingBook.setAuthors(authors);
        existingBook.setCategories(categories);
        existingBook.setEditor(editor);
        existingBook.getImages().clear();
        existingBook.getImages().addAll(images);

        return bookMapper.toDto(existingBook);
    }

    public void delete(Long id) {

        Book book = bookRepository.findById(id).orElseThrow(() -> new NotFoundException("Livre introuvable."));

        if (!book.isAvailable()) {
            throw new BadRequestException("Impossible de supprimer un livre actuellement emprunté.");
        }

        if(!book.isOwned()){
            throw new BadRequestException("Impossible de supprimer un livre externe.");
        }

        bookRepository.delete(book);
    }

    public BookResponseDTO findByTitle(String title) {

        Book book = bookRepository.findByTitleIgnoreCase(title)
                .orElseThrow(() -> new NotFoundException("Livre introuvable."));

        return bookMapper.toDto(book);
    }

    public BookResponseDTO findByIsbn(String isbn) {

        Book book = bookRepository.findByIsbn(isbn)
                .orElseThrow(() -> new NotFoundException("Livre introuvable."));

        return bookMapper.toDto(book);
    }

    public Page<BookResponseDTO> findByAvailable(boolean available, Pageable pageable) {

        return bookRepository.findByAvailable(available, pageable)
                .map(bookMapper::toDto);
    }

    public Page<BookResponseDTO> findByAuthor(Long authorId, Pageable pageable) {

        return bookRepository.findByAuthorsId(authorId, pageable)
                .map(bookMapper::toDto);
    }

    public Page<BookResponseDTO> findByCategory(Long categoryId, Pageable pageable) {

        return bookRepository.findByCategoriesId(categoryId, pageable)
                .map(bookMapper::toDto);
    }

    public Page<BookResponseDTO> findByEditor(Long editorId, Pageable pageable) {

        return bookRepository.findByEditorId(editorId, pageable)
                .map(bookMapper::toDto);
    }

    public Page<BookResponseDTO> search(
            String title, Boolean available, String authorName, Pageable pageable) {

        return bookRepository.search(
                title,
                available,
                authorName,
                pageable
        ).map(bookMapper::toDto);
    }

}
