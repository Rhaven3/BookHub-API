package fr.eni.td2j.bookhub_api.author;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepository repository;

    public AuthorServiceImpl(AuthorRepository repository) {
        this.repository = repository;
    }

    @Override
    public Page<Author> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Optional<Author> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Author create(Author author) {

        if (author.getId() != null) {
            throw new IllegalArgumentException("L'id doit être null.");
        }

        if (repository.existsByFnameIgnoreCaseAndLnameIgnoreCase(author.getFname(), author.getLname())) {
            throw new IllegalArgumentException("Cet auteur existe déjà.");
        }
        return repository.save(author);
    }

    @Override
    public Author update(Long id, Author author) {

        if (!repository.existsById(id)) {
            throw new IllegalArgumentException("Auteur introuvable.");
        }
        author.setId(id);
        return repository.save(author);
    }

    @Override
    public void delete(Long id) {

        if (!repository.existsById(id)) {
            throw new IllegalArgumentException("Auteur introuvable.");
        }
        repository.deleteById(id);

    }
}
