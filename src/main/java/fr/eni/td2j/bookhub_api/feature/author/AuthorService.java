package fr.eni.td2j.bookhub_api.feature.author;

import fr.eni.td2j.bookhub_api.exception.BadRequestException;
import fr.eni.td2j.bookhub_api.exception.NotFoundException;
import fr.eni.td2j.bookhub_api.feature.author.dto.request.AuthorRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

        if (repository.existsByFirstNameIgnoreCaseAndLastNameIgnoreCase(author.getFirstName(), author.getLastName())) {
            throw new BadRequestException("Cet auteur existe déjà.");
        }

        return repository.save(author);
    }

    public Author update(Long id, Author author) {

        Author existingAuthor = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Auteur introuvable."));

        boolean duplicate = repository.existsByFirstNameIgnoreCaseAndLastNameIgnoreCase(
                author.getFirstName(),
                author.getLastName()
        );

        boolean nameChanged =
                !existingAuthor.getFirstName().equalsIgnoreCase(author.getFirstName())
                        || !existingAuthor.getLastName().equalsIgnoreCase(author.getLastName());


        if (duplicate && nameChanged) {
            throw new BadRequestException("Cet auteur existe déjà.");
        }

        existingAuthor.setFirstName(author.getFirstName());
        existingAuthor.setLastName(author.getLastName());

        return repository.save(existingAuthor);
    }

    public void deleteById(Long id) {

        if (!repository.existsById(id)) {
            throw new NotFoundException("Auteur introuvable.");
        }
        repository.deleteById(id);

    }

    Page<Author> search(String search, Pageable pageable) {
        return repository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(search, search, pageable);
    }

    public List<Author> createAuthors(List<AuthorRequestDTO> newAuthorDTOs) {
        if (newAuthorDTOs == null || newAuthorDTOs.isEmpty()) {
            return List.of();
        }

        return newAuthorDTOs.stream()
                .map(dto -> Author.builder()
                        .firstName(dto.getFirstName())
                        .lastName(dto.getLastName())
                        .build())
                .map(repository::save)
                .collect(Collectors.toList());
    }
}
