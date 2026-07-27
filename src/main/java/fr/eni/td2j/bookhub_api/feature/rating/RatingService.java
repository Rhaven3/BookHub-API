package fr.eni.td2j.bookhub_api.feature.rating;

import fr.eni.td2j.bookhub_api.exception.BadRequestException;
import fr.eni.td2j.bookhub_api.exception.NotFoundException;
import fr.eni.td2j.bookhub_api.feature.book.Book;
import fr.eni.td2j.bookhub_api.feature.book.BookRepository;
import fr.eni.td2j.bookhub_api.feature.user.User;
import fr.eni.td2j.bookhub_api.feature.user.UserRepository;
import fr.eni.td2j.bookhub_api.feature.user.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class RatingService {

    private final RatingRepository ratingRepository;
    private final UserService userService;
    private final BookRepository bookRepository;

    public RatingService(RatingRepository ratingRepository, UserService userService, BookRepository bookRepository) {
        this.ratingRepository = ratingRepository;
        this.userService = userService;
        this.bookRepository = bookRepository;
    }

    public Page<Rating> findAll(Pageable pageable) {
        return ratingRepository.findAll(pageable);
    }

    public Optional<Rating> findById(Long id) {
        return ratingRepository.findById(id);
    }

    public Rating create(Rating rating, UserDetails userDetails) {
        if (rating.getId() != null) {
            throw new BadRequestException("L'id doit être null.");
        }

        rating.setUser(userService.getConnectedUser(userDetails));
        rating.setBook(getBook(rating));
        rating.setDate(LocalDateTime.now());
        rating.setStatus(RatingEnum.PUBLISH);

        return ratingRepository.save(rating);
    }

    public Rating update(Rating rating, UserDetails userDetails) {
        Rating existingRating = findById(rating.getId())
                .orElseThrow(() -> new NotFoundException("Note non trouvée."));

        if (rating.getId() != null && rating.getId().equals(existingRating.getId())) {
            throw new BadRequestException("L'id de l'avis ne correspond pas à l'URL.");
        }

        existingRating.setCommentary(rating.getCommentary());
        existingRating.setNote(rating.getNote());
        existingRating.setStatus(rating.getStatus());
        existingRating.setDate(LocalDateTime.now());

        existingRating.setUser(userService.getConnectedUser(userDetails));
        existingRating.setBook(getBook(rating));

        return ratingRepository.save(existingRating);
    }

    public void delete(Long id) {
        Rating rating = ratingRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Note non trouvée."));

        ratingRepository.delete(rating);
    }

    private Book getBook(Rating rating) {
        return bookRepository.findById(rating.getBook().getId())
                .orElseThrow(() -> new NotFoundException("Livre non trouvé."));
    }

}
