package fr.eni.td2j.bookhub_api.feature.rating;

import fr.eni.td2j.bookhub_api.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
    private RatingEnum status;
}
