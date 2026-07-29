package fr.eni.td2j.bookhub_api.feature.author;

import fr.eni.td2j.bookhub_api.exception.BadRequestException;
import fr.eni.td2j.bookhub_api.exception.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthorService {
    private final AuthorRepository repository;

    public AuthorService(AuthorRepository repository) {
        this.repository = repository;
    }

    public Page<Author> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Optional<Author> findById(Long id) {
        return repository.findById(id);
    }

    public Author create(Author author) {

        if (author.getId() != null) {
            throw new BadRequestException("L'id doit être null.");
        }

        if (repository.existsByFnameIgnoreCaseAndLnameIgnoreCase(author.getFname(), author.getLname())) {
            throw new BadRequestException("Cet auteur existe déjà.");
        }

        return repository.save(author);
    }

    public Author update(Long id, Author author) {

        Author existingAuthor = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Auteur introuvable."));

        boolean duplicate = repository.existsByFnameIgnoreCaseAndLnameIgnoreCase(
                author.getFname(),
                author.getLname()
        );

        boolean nameChanged =
                !existingAuthor.getFname().equalsIgnoreCase(author.getFname())
                        || !existingAuthor.getLname().equalsIgnoreCase(author.getLname());


        if (duplicate && nameChanged) {
            throw new BadRequestException("Cet auteur existe déjà.");
        }

        existingAuthor.setFname(author.getFname());
        existingAuthor.setLname(author.getLname());

        return repository.save(existingAuthor);
    }

    public void deleteById(Long id) {

        if (!repository.existsById(id)) {
            throw new NotFoundException("Auteur introuvable.");
        }
        repository.deleteById(id);

    }

    Page<Author> search(String search, Pageable pageable) {
        return repository.findByFnameContainingIgnoreCaseOrLnameContainingIgnoreCase(search, search, pageable);
    }
}
