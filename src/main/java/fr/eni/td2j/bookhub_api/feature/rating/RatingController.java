package fr.eni.td2j.bookhub_api.feature.rating;

import fr.eni.td2j.bookhub_api.common.ApiResponse;
import fr.eni.td2j.bookhub_api.common.dtos.page.PageResponseDTO;
import fr.eni.td2j.bookhub_api.common.dtos.page.mapper.PageMapper;
import fr.eni.td2j.bookhub_api.exception.NotFoundException;
import fr.eni.td2j.bookhub_api.feature.rating.DTO.RatingCreateDTO;
import fr.eni.td2j.bookhub_api.feature.rating.DTO.RatingUpdateDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/rating")
public class RatingController {
     private final RatingService ratingService;
     public RatingController(RatingService ratingService) {
        this.ratingService = ratingService;
     }

    @GetMapping public ResponseEntity<ApiResponse<PageResponseDTO<Rating>>> findAll(
            Pageable pageable,
            @AuthenticationPrincipal UserDetails userDetails) {
         return ResponseEntity.ok(
                 ApiResponse.success( PageMapper.toDto( ratingService.findAll(pageable, userDetails) ), "Avis recuperés avec succes")
         );
     }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Rating>> findById(@PathVariable Long id) {
        Rating rating = ratingService.findById(id)
                .orElseThrow(() -> new NotFoundException("Avis introuvable"));

        return ResponseEntity.ok(
                ApiResponse.success(rating, "Avis récupéré avec succès")
        );
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Rating>> create(
            @RequestBody RatingCreateDTO rating,
            @AuthenticationPrincipal UserDetails userDetails) {

        Rating created = ratingService.create(rating, userDetails);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.success(created, "Avis créé avec succès")
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Rating>> update(
            @PathVariable Long id,
            @RequestBody RatingUpdateDTO rating,
            @AuthenticationPrincipal UserDetails userDetails) {

        Rating updated = ratingService.update(id, rating, userDetails);

        return ResponseEntity.ok(
                ApiResponse.success(updated, "Avis modifié avec succès")
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {

        ratingService.delete(id, userDetails);

        return ResponseEntity.ok(
                ApiResponse.success(null, "Avis supprimé avec succès")
        );
    }

    @GetMapping("/book/{bookId}")
    public ResponseEntity<ApiResponse<Page<Rating>>> findByBook(
            @PathVariable Long bookId,
            Pageable pageable) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        ratingService.findByBook(bookId, pageable),
                        "Avis du livre récupérés avec succès."
                )
        );
    }
}
