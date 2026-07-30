package fr.eni.td2j.bookhub_api.feature.book;

import fr.eni.td2j.bookhub_api.common.ApiResponse;
import fr.eni.td2j.bookhub_api.common.dtos.page.PageResponseDTO;
import fr.eni.td2j.bookhub_api.common.dtos.page.mapper.PageMapper;
import fr.eni.td2j.bookhub_api.feature.book.dto.BookRequestDTO;
import fr.eni.td2j.bookhub_api.feature.book.dto.BookResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/book")
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

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<BookResponseDTO>> create(
            @RequestPart("book") @Valid BookRequestDTO book,
            @RequestPart(value = "files", required = false) List<MultipartFile> files
    ) {
        return ResponseEntity.ok(ApiResponse.success(bookService.create(book, files), "Livre créé avec succès."));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<BookResponseDTO>> update(
            @PathVariable Long id,
            @RequestPart("book") @Valid BookRequestDTO book,
            @RequestPart(value = "files", required = false) List<MultipartFile> files
    ) {
        return ResponseEntity.ok(
                ApiResponse.success(
                        bookService.update(id, book, files),
                        "Livre modifié avec succès"
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

    @GetMapping("/search")
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

    @GetMapping("/search/title")
    public ResponseEntity<ApiResponse<BookResponseDTO>> getByTitle(@RequestParam("titre") String title) {
        return ResponseEntity.ok(
                ApiResponse.success(
                        bookService.findByTitle(title),
                        "Livre trouvé avec succes."
                )
        );
    }

    @GetMapping("/search/isbn")
    public ResponseEntity<ApiResponse<BookResponseDTO>> getByIsbn(@RequestParam String isbn) {
        return ResponseEntity.ok(
                ApiResponse.success(
                        bookService.findByIsbn(isbn),
                        "Livre trouvé avec succes."
                )
        );
    }

    @GetMapping("/available")
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

    @GetMapping("/authors/{authorId}")
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

    @GetMapping("/category/{categoryId}")
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

    @GetMapping("/editors/{editorId}")
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

    @GetMapping("/filter")
    public ResponseEntity<ApiResponse<PageResponseDTO<BookResponseDTO>>> filter(
            @RequestParam(required = false) Long authorId,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Long editorId,
            Pageable pageable
    ) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        PageMapper.toDto(
                                bookService.filter(authorId, categoryId, editorId, pageable)
                        ),
                        "Livres trouvés avec succès."
                )
        );
    }
}
