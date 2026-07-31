package fr.eni.td2j.bookhub_api.feature.rating;

import fr.eni.td2j.bookhub_api.feature.book.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RatingRepository extends JpaRepository<Rating, Long> {
    Page<Rating> findByStatus(RatingEnum ratingEnum, Pageable pageable);
    Page<Rating> findByBook(Book book, Pageable pageable);

}
