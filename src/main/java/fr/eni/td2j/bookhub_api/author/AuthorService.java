package fr.eni.td2j.bookhub_api.author;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface AuthorService {

    Page<Author> findAll(Pageable pageable);
    Optional<Author> findById(Long id);
    Author create(Author author);
    Author update(Long id, Author author);
    void delete(Long id);

}
