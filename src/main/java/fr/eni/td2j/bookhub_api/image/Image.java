package fr.eni.td2j.bookhub_api.image;

import fr.eni.td2j.bookhub_api.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode(callSuper = false)
@Getter
@Setter

@Entity
public class Image extends BaseEntity {

    @Lob
    @Column(nullable = false)
    private String path;

    @Column(unique = true, nullable = false)
    private String name;

}
