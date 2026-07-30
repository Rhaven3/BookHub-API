package fr.eni.td2j.bookhub_api.feature.author;

import fr.eni.td2j.bookhub_api.common.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
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
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"firstName", "lastName"}))
public class Author extends BaseEntity {
    @NotBlank(message = "Le prénom est obligatoire.")
    @Column(nullable = false)
    private String firstName;
    @NotBlank(message = "Le nom est obligatoire.")
    @Column(nullable = false)
    private String lastName;

}
