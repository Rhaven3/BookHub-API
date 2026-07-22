package fr.eni.td2j.bookhub_api.controller;

import fr.eni.td2j.bookhub_api.entity.Author;
import fr.eni.td2j.bookhub_api.repository.AuthorRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/author")
public class AuthorController {

    private final AuthorRepository authorRepository;

    public AuthorController(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @GetMapping("/list")
    public ResponseEntity<List<Author>> findAll() {
        List<Author> authors = authorRepository.findAll();
        return ResponseEntity.ok().body(authors);

    }

    @GetMapping("/{id}")
    public ResponseEntity<Author> findById(@PathVariable Long id) {
        Optional<Author> author = authorRepository.findById(id);
        return author.map(value -> ResponseEntity.ok().body(value)).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/create")
    public ResponseEntity<?>  create(@RequestBody Author author) {
        if (authorRepository.findById(author.getId()).isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("l'id doit etre null " + author.getId());
        }

        try {
            authorRepository.save(author);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Error creating author" + e.getMessage());
        }

        return ResponseEntity.ok("le auteur est crée");

    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Author author) {
        if (authorRepository.findById(id).isPresent()) {
            author.setId(id);
        }
        try {
            authorRepository.save(author);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Error updating author" + e.getMessage());
        }
        return ResponseEntity.ok("le auteur est modifié");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            authorRepository.deleteById(id);
            return ResponseEntity.ok("auteur supprimé");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Error deleting author" + e.getMessage());
        }
    }
}
