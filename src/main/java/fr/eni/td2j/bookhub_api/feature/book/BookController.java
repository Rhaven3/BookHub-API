package fr.eni.td2j.bookhub_api.feature.book;

import fr.eni.td2j.bookhub_api.common.ApiResponse;
import fr.eni.td2j.bookhub_api.common.dtos.page.PageResponseDTO;
import fr.eni.td2j.bookhub_api.common.dtos.page.mapper.PageMapper;
import fr.eni.td2j.bookhub_api.feature.book.dto.BookRequestDTO;
import fr.eni.td2j.bookhub_api.feature.book.dto.BookResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/livres")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponseDTO<BookResponseDTO>>> findAll(Pageable pageable) {
        return ResponseEntity.ok(
                ApiResponse.success(
                        PageMapper.toDto(
                                bookService.findAll(pageable)
                        ),
                        "Livres récupéré avec succes."
                )
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BookResponseDTO>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(
                ApiResponse.success(bookService.findById(id), "Livre récupéré avec succès.")
        );
    }

    @PostMapping
    public ResponseEntity<ApiResponse<BookResponseDTO>> create(@Valid @RequestBody BookRequestDTO book) {
        return ResponseEntity.ok(
                ApiResponse.success(
                        bookService.create(book),
                        "Livre crée avec succes."
                )
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<BookResponseDTO>> update(
            @PathVariable Long id, @Valid @RequestBody BookRequestDTO book) {
        return ResponseEntity.ok(
                ApiResponse.success(
                        bookService.update(id, book),
                        "Livre modifié avec succes"
                )
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<BookResponseDTO>> delete(@PathVariable Long id) {
        bookService.delete(id);
        return ResponseEntity.ok(
                ApiResponse.success(
                        null,
                        "Livre supprimé avec succes"
                )
        );
    }

    @GetMapping("/recherche")
    public ResponseEntity<ApiResponse<PageResponseDTO<BookResponseDTO>>> search(
            @RequestParam("je-cherche") String word,
            Pageable pageable
    ) {
        return ResponseEntity.ok(
                ApiResponse.success(
                        PageMapper.toDto(bookService.search(word, pageable)),
                        "Livres trouvés avec succès."
                )
        );
    }

    @GetMapping("/recherche/titre")
    public ResponseEntity<ApiResponse<BookResponseDTO>> getByTitle(@RequestParam("titre") String title) {
        return ResponseEntity.ok(
                ApiResponse.success(
                        bookService.findByTitle(title),
                        "Livre trouvé avec succes."
                )
        );
    }

    @GetMapping("/recherche/isbn")
    public ResponseEntity<ApiResponse<BookResponseDTO>> getByIsbn(@RequestParam String isbn) {
        return ResponseEntity.ok(
                ApiResponse.success(
                        bookService.findByIsbn(isbn),
                        "Livre trouvé avec succes."
                )
        );
    }

    @GetMapping("/dispo")
    public ResponseEntity<ApiResponse<PageResponseDTO<BookResponseDTO>>> getByAvailable(
            @RequestParam("dispo") Boolean available, Pageable pageable) {
        return ResponseEntity.ok(
                ApiResponse.success(
                        PageMapper.toDto(
                                bookService.findByAvailable(available, pageable)
                        ),
                        "Livres trouvés avec succès."
                )
        );
    }

    @GetMapping("/auteurs/{authorId}")
    public ResponseEntity<ApiResponse<PageResponseDTO<BookResponseDTO>>> getByAuthor(
            @PathVariable Long authorId, Pageable pageable) {
        return ResponseEntity.ok(
                ApiResponse.success(
                        PageMapper.toDto(
                                bookService.findByAuthor(authorId, pageable)
                        ),
                        "Livres trouvés avec succès."
                )
        );
    }

    @GetMapping("/categories/{categoryId}")
    public ResponseEntity<ApiResponse<PageResponseDTO<BookResponseDTO>>> getByCategory(
            @PathVariable Long categoryId, Pageable pageable) {
        return ResponseEntity.ok(
                ApiResponse.success(
                        PageMapper.toDto(
                                bookService.findByCategory(categoryId, pageable)
                        ),
                        "Livres trouvés avec succès."
                )
        );
    }

    @GetMapping("/editeurs/{editorId}")
    public ResponseEntity<ApiResponse<PageResponseDTO<BookResponseDTO>>> getByEditor(
            @PathVariable Long editorId, Pageable pageable) {
        return ResponseEntity.ok(
                ApiResponse.success(
                        PageMapper.toDto(
                                bookService.findByEditor(editorId, pageable)
                        ),
                        "Livres trouvés avec succès."
                )
        );
    }
}
