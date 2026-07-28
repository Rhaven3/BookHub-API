package fr.eni.td2j.bookhub_api.feature.rating;

import fr.eni.td2j.bookhub_api.exception.BadRequestException;
import fr.eni.td2j.bookhub_api.exception.NotFoundException;
import fr.eni.td2j.bookhub_api.feature.book.Book;
import fr.eni.td2j.bookhub_api.feature.book.BookRepository;
import fr.eni.td2j.bookhub_api.feature.rating.DTO.RatingCreateDTO;
import fr.eni.td2j.bookhub_api.feature.rating.DTO.RatingUpdateDTO;
import fr.eni.td2j.bookhub_api.feature.user.Role;
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

    public Page<Rating> findAll(Pageable pageable, UserDetails userDetails) {

        User conectedUser = userService.getConnectedUser(userDetails);

        if (conectedUser != null && "ADMIN".equals(conectedUser.getRole())) {
            return ratingRepository.findAll(pageable);
        }

        return ratingRepository.findByStatus(RatingEnum.PUBLISH, pageable);
    }

    public Optional<Rating> findById(Long id) {
        return ratingRepository.findById(id);
    }

    public Rating create(RatingCreateDTO dto, UserDetails userDetails) {

        User user = userService.getConnectedUser(userDetails);

        Book book = bookRepository.findById(dto.getBookId())
                .orElseThrow(() -> new NotFoundException("Livre introuvable."));

        Rating rating = Rating.builder()
                .note(dto.getNote())
                .commentary(dto.getCommentary())
                .date(LocalDateTime.now())
                .status(RatingEnum.PUBLISH)
                .book(book)
                .user(user)
                .build();

        return ratingRepository.save(rating);
    }

    public Rating update(Long id, RatingUpdateDTO rating, UserDetails userDetails) {
        Rating existingRating = findById(id)
                .orElseThrow(() -> new NotFoundException("Note non trouvée."));

        if (!existingRating.getUser().getEmail().equals(userDetails.getUsername())) {
            throw new BadRequestException("Vous ne pouvez modifier que vos propres avis.");
        }

        existingRating.setCommentary(rating.getCommentary());
        existingRating.setNote(rating.getNote());
        existingRating.setDate(LocalDateTime.now());

        return ratingRepository.save(existingRating);
    }

    public void delete(Long id, UserDetails userDetails) {

        Rating rating = ratingRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Avis introuvable."));

        User connectedUser =  userService.getConnectedUser(userDetails);

        boolean isAdmin = connectedUser.getRole().equals("ADMIN");

        boolean isOwner = rating.getUser().getId().equals(connectedUser.getId());

        if (!isAdmin && !isOwner) {
            throw new BadRequestException("Vous ne pouvez supprimer que vos propres avis.");
        }

        rating.setStatus(RatingEnum.CANCELED);

        ratingRepository.save(rating);



    }

    private Book getBook(Rating rating) {
        return bookRepository.findById(rating.getBook().getId())
                .orElseThrow(() -> new NotFoundException("Livre non trouvé."));
    }

}
