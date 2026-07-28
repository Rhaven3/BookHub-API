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

import java.time.LocalDate;
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
                .publishDate(LocalDate.from(requestDTO.getPublishDate()))
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

        Book existingBook = bookRepository.findById(id).orElseThrow(() -> new NotFoundException("Livre introuvable."));

        if (requestBook.getTitle() != null) {
            existingBook.setTitle(requestBook.getTitle());
        }

        if (requestBook.getDescription() != null) {
            existingBook.setDescription(requestBook.getDescription());
        }

        if (requestBook.getPublishDate() != null) {
            existingBook.setPublishDate(requestBook.getPublishDate());
        }

        if (requestBook.getLanguage() != null) {
            existingBook.setLanguage(requestBook.getLanguage());
        }

        if (requestBook.getIsbn() != null) {
            existingBook.setIsbn(requestBook.getIsbn());
        }

        if (requestBook.getEditorId() != null) {
            existingBook.setEditor(getEditor(requestBook.getEditorId()));
        }

        if (requestBook.getAuthorIds() != null) {
            existingBook.setAuthors(getAuthors(requestBook.getAuthorIds()));
        }

        if (requestBook.getCategoryIds() != null) {
            existingBook.setCategories(getCategories(requestBook.getCategoryIds()));
        }

        if (requestBook.getImageIds() != null) {
            List<Image> images = getImages(requestBook.getImageIds());
            existingBook.getImages().clear();
            existingBook.getImages().addAll(images);
        }

        return bookMapper.toDto(bookRepository.save(existingBook));
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

        if(title == null || title.isBlank()){
            throw new BadRequestException("Le titre du livre est obligatoire.");
        }

        Book book = bookRepository.findByTitleIgnoreCase(title)
                .orElseThrow(() -> new NotFoundException("Livre introuvable."));

        return bookMapper.toDto(book);
    }

    public BookResponseDTO findByIsbn(String isbn) {

        if(isbn == null || isbn.isBlank()){
            throw new BadRequestException("L'ISBN du livre est obligatoire.");
        }

        Book book = bookRepository.findByIsbn(isbn)
                .orElseThrow(() -> new NotFoundException("Livre introuvable."));

        return bookMapper.toDto(book);
    }

    public Page<BookResponseDTO> findByAvailable(boolean available, Pageable pageable) {

        Page<Book> books = bookRepository.findByAvailable(available, pageable);

        if (books.isEmpty()) {
            throw new NotFoundException(
                    "Aucun livre trouvé avec ce statut de disponibilité."
            );
        }

        return books.map(bookMapper::toDto);
    }

    public Page<BookResponseDTO> findByAuthor(Long authorId, Pageable pageable) {

        Page<Book> books = bookRepository.findByAuthorsId(authorId, pageable);

        if (books.isEmpty()) {
            throw new NotFoundException("Aucun livre trouvé pour cet auteur.");
        }

        return books.map(bookMapper::toDto);
    }

    public Page<BookResponseDTO> findByCategory(Long categoryId, Pageable pageable) {

        Page<Book> books = bookRepository.findByCategoriesId(categoryId, pageable);

        if (books.isEmpty()) {
            throw new NotFoundException("Aucun livre trouvé pour cette catégorie.");
        }

        return books.map(bookMapper::toDto);
    }

    public Page<BookResponseDTO> findByEditor(Long editorId, Pageable pageable) {

        Page<Book> books = bookRepository.findByEditorId(editorId, pageable);

        if (books.isEmpty()) {
            throw new NotFoundException("Aucun livre trouvé pour cet éditeur.");
        }

        return books.map(bookMapper::toDto);
    }

    public Page<BookResponseDTO> search(String word, Pageable pageable) {

        if (word == null || word.isBlank()) {
            throw new BadRequestException(
                    "Le mot de recherche est obligatoire."
            );
        }

        Page<Book> books = bookRepository.search(word, pageable);

        if (books.isEmpty()) {
            throw new NotFoundException(
                    "Aucun livre trouvé pour cette recherche."
            );
        }

        return books.map(bookMapper::toDto);
    }

}
