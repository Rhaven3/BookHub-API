package fr.eni.td2j.bookhub_api.feature.editor;

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
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"name"}))
public class Editor extends BaseEntity {
    @NotBlank(message = "Le nom est obligatoire.")
    @Column(nullable = false)
    private String name;
}
