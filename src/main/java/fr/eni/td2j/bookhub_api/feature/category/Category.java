package fr.eni.td2j.bookhub_api.feature.category;

import fr.eni.td2j.bookhub_api.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"name"}))
public class Category extends BaseEntity {
    @Column(nullable = false)
    private String name;
}
