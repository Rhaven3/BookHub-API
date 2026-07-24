package fr.eni.td2j.bookhub_api.feature.book;

import fr.eni.td2j.bookhub_api.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode(callSuper = true)
@Builder
public class Book extends BaseEntity {

    @Column(nullable = false, length = 255)
    private String title;

    @Column(length = 2000)
    private String description;

    @Column(nullable = false)
    private LocalDate publishDate;

    @Column(nullable = false, length = 50)
    private String language;

    @Column(nullable = false, unique = true, length = 17)
    private String isbn;

    @Column(nullable = false)
    private boolean available;

    @Column(nullable = false)
    private boolean owned;
}
