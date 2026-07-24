package fr.eni.td2j.bookhub_api.feature.author;

import fr.eni.td2j.bookhub_api.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode(callSuper = true)
@Builder
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"fname", "lname"}))
public class Author extends BaseEntity {
    @Column(nullable = false)
    private String fname;
    @Column(nullable = false)
    private String lname;

}
