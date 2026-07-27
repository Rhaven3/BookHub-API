package fr.eni.td2j.bookhub_api.feature.book;

import fr.eni.td2j.bookhub_api.exception.BadRequestException;
import fr.eni.td2j.bookhub_api.exception.NotFoundException;
import fr.eni.td2j.bookhub_api.feature.author.Author;
import fr.eni.td2j.bookhub_api.feature.author.AuthorRepository;
import fr.eni.td2j.bookhub_api.feature.category.Category;
import fr.eni.td2j.bookhub_api.feature.category.CategoryRepository;
import fr.eni.td2j.bookhub_api.feature.editor.Editor;
import fr.eni.td2j.bookhub_api.feature.editor.EditorRepository;
import fr.eni.td2j.bookhub_api.feature.image.Image;
import fr.eni.td2j.bookhub_api.feature.image.ImageRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final CategoryRepository categoryRepository;
    private final EditorRepository editorRepository;
    private final ImageRepository imageRepository;

    public BookService(BookRepository bookRepository, AuthorRepository authorRepository, CategoryRepository categoryRepository, EditorRepository editorRepository, ImageRepository imageRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.categoryRepository = categoryRepository;
        this.editorRepository = editorRepository;
        this.imageRepository = imageRepository;
    }

    public Page<Book> findAll(Pageable pageable) {
        return bookRepository.findAll(pageable);
    }

    public Optional<Book> findById(Long id) {
        return bookRepository.findById(id);
    }

    public Book create(Book book) {
        if (book.getId() != null) {
            throw new BadRequestException("L'id doit être null.");
        }

        // Authors
        book.setAuthors(getAuthors(book));
        // Categories
        book.setCategories(getCategories(book));
        // Editor
        book.setEditor(getEditor(book));

        return bookRepository.save(book);

    }

    private List<Category> getCategories(Book book) {

        List<Long> categoryIds = book.getCategories().stream()
                 .map(Category::getId)
                 .toList();

        List<Category> categories = categoryRepository.findAllById(categoryIds);

        if (categories.size() != categoryIds.size()) {
            throw new NotFoundException("Une ou plusieurs catégories sont introuvables.");
        }
        return categories;
    }

    private List<Author> getAuthors(Book book) {

        List<Long> authorIds = book.getAuthors().stream()
                .map(Author::getId)
                .toList();

        List<Author> authors = authorRepository.findAllById(authorIds);

        if (authors.size() != authorIds.size()) {
            throw new NotFoundException("Un ou plusieurs auteurs sont introuvables.");
        }
        return authors;
    }

    private Editor getEditor(Book book) {
        return editorRepository.findById(book.getEditor().getId())
                .orElseThrow(() -> new NotFoundException("Éditeur introuvable."));
    }

    public Book update(Long id, Book book) {

        Book existingBook = bookRepository.findById(book.getId())
                .orElseThrow(() -> new NotFoundException("Livre introuvable."));

        if (book.getId() != null && !book.getId().equals(id)) {
            throw new BadRequestException("L'id du livre ne correspond pas à l'URL.");
        }

        existingBook.setTitle(book.getTitle());
        existingBook.setDescription(book.getDescription());
        existingBook.setPublishDate(book.getPublishDate());
        existingBook.setLanguage(book.getLanguage());
        existingBook.setIsbn(book.getIsbn());
        existingBook.setAvailable(book.isAvailable());
        existingBook.setOwned(book.isOwned());
        // Authors
        existingBook.setAuthors(getAuthors(book));
        // Categories
        existingBook.setCategories(getCategories(book));
        // Editor
        existingBook.setEditor(getEditor(book));

        //Images
        existingBook.getImages().clear();
        existingBook.getImages().addAll(book.getImages());

        return bookRepository.save(existingBook);
    }

    public void delete(Long id) {

        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Livre introuvable."));

        if (!book.isAvailable()) {
            throw new BadRequestException(
                    "Impossible de supprimer un livre actuellement emprunté.");
        }
        bookRepository.delete(book);
    }
}
