package fr.eni.td2j.bookhub_api.feature.rating;

import fr.eni.td2j.bookhub_api.feature.rating.DTO.RatingCreateDTO;
import fr.eni.td2j.bookhub_api.feature.rating.DTO.RatingUpdateDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @GetMapping
    public ResponseEntity<Page<Rating>> findAll(Pageable pageable) {
        return ResponseEntity.ok(ratingService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Rating> findById(@PathVariable Long id) {
        return ratingService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody RatingCreateDTO rating, @AuthenticationPrincipal UserDetails userDetails) {
        try {
            Rating created = ratingService.create(rating, userDetails);
            return ResponseEntity.status(201).body(created);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id,
                                    @RequestBody RatingUpdateDTO rating,
                                    @AuthenticationPrincipal UserDetails userDetails
    ) {
        try {
            Rating updated = ratingService.update(id, rating, userDetails);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {

        try {
            ratingService.delete(id, userDetails);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

}
