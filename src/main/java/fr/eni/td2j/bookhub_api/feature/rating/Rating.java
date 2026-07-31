package fr.eni.td2j.bookhub_api.feature.rating;

import fr.eni.td2j.bookhub_api.common.BaseEntity;
import fr.eni.td2j.bookhub_api.feature.book.Book;
import fr.eni.td2j.bookhub_api.feature.user.User;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@SuperBuilder
@Entity
public class Rating extends BaseEntity {
    @Column(nullable = false)
    private int note;

    private String commentary;
    @Column(nullable = false)

    private LocalDateTime date;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private RatingEnum status;

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
