package fr.eni.td2j.bookhub_api.feature.category;

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
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"name"}))
public class Category extends BaseEntity {
    @Column(nullable = false)
    private String name;
}
