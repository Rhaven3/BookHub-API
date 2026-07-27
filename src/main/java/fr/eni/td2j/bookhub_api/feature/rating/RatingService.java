package fr.eni.td2j.bookhub_api.feature.rating;

import fr.eni.td2j.bookhub_api.exception.BadRequestException;
import fr.eni.td2j.bookhub_api.exception.NotFoundException;
import fr.eni.td2j.bookhub_api.feature.book.Book;
import fr.eni.td2j.bookhub_api.feature.book.BookRepository;
import fr.eni.td2j.bookhub_api.feature.user.User;
import fr.eni.td2j.bookhub_api.feature.user.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class RatingService {

    private final RatingRepository ratingRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    public RatingService(RatingRepository ratingRepository, UserRepository userRepository, BookRepository bookRepository) {
        this.ratingRepository = ratingRepository;
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
    }

    public Page<Rating> findAll(Pageable pageable) {
        return ratingRepository.findAll(pageable);
    }

    public Optional<Rating> findById(Long id) {
        return ratingRepository.findById(id);
    }

    public Rating create(Rating rating) {
        if (rating.getId() != null) {
            throw new BadRequestException("L'id doit être null.");
        }

        rating.setUser(getUser(rating));
        rating.setBook(getBook(rating));
        rating.setDate(LocalDateTime.now());

        return ratingRepository.save(rating);
    }

    public Rating update(Rating rating) {
        Rating existingRating = findById(rating.getId())
                .orElseThrow(() -> new NotFoundException("Note non trouvée."));

        if (rating.getId() != null && rating.getId().equals(existingRating.getId())) {
            throw new BadRequestException("L'id de l'avis ne correspond pas à l'URL.");
        }

        existingRating.setCommentary(rating.getCommentary());
        existingRating.setNote(rating.getNote());
        existingRating.setStatus(rating.getStatus());
        existingRating.setDate(rating.getDate());

        existingRating.setUser(getUser(rating));
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

    private User getUser(Rating rating) {
        return userRepository.findById(rating.getUser().getId())
                .orElseThrow(() -> new NotFoundException("Utilisateur non trouvé."));
    }
}
