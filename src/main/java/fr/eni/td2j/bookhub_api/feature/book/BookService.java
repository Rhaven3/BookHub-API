package fr.eni.td2j.bookhub_api.feature.book;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public Page<Book> findAll(Pageable pageable) {
        return bookRepository.findAll(pageable);
    }

    public Optional<Book> findById(Long id) {
        return bookRepository.findById(id);
    }

    public Book create(Book book) {
        if (book.getId() != null) {
            throw new IllegalArgumentException("L'id doit être null.");
        }

        if (bookRepository.existsById(book.getId())) {
            throw new IllegalArgumentException("Cet livre existe déjà.");
        }
        return bookRepository.save(book);
    }

    public Book update(Long id, Book book) {
        if (!bookRepository.existsById(id)) {
            throw new IllegalArgumentException("Livre introuvable.");
        }
        book.setId(id);
        return bookRepository.save(book);
    }

    public void delete(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new IllegalArgumentException("Livre introuvable.");
        }
    }
}
