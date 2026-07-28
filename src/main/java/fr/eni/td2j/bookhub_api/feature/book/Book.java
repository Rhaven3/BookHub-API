package fr.eni.td2j.bookhub_api.feature.book;

import fr.eni.td2j.bookhub_api.common.BaseEntity;
import fr.eni.td2j.bookhub_api.feature.author.Author;
import fr.eni.td2j.bookhub_api.feature.category.Category;
import fr.eni.td2j.bookhub_api.feature.editor.Editor;
import fr.eni.td2j.bookhub_api.feature.image.Image;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class Book extends BaseEntity {

    @NotBlank(message = "Le titre est obligatoire")
    @Column(nullable = false)
    private String title;

    @Size(max = 2000, message = "La description ne peut pas dépasser 2000 caractères")
    @Column(length = 2000)
    private String description;

    @NotNull(message = "La date de publication est obligatoire")
    @PastOrPresent(message = "La date de publication ne peut pas être dans le futur")
    @Column(nullable = false)
    private Instant publishDate;

    @NotBlank(message = "La langue est obligatoire")
    @Column(nullable = false, length = 50)
    private String language;

    @NotBlank(message = "L'ISBN est obligatoire")
    @Pattern(regexp = "^(97(8|9))?\\d{9}(\\d|X)$", message = "Format d'ISBN invalide")
    @Column(nullable = false, unique = true, length = 17)
    private String isbn;

    @Column(nullable = false)
    private boolean available;

    @Column(nullable = false)
    private boolean owned;

    @NotEmpty(message = "Le livre doit avoir au moins un auteur")
    @ManyToMany
    @JoinTable(
            name = "book_author",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id")
    )
    private List<Author> authors = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "book_category",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private List<Category> categories = new ArrayList<>();

    @NotNull(message = "L'éditeur est obligatoire.")
    @ManyToOne
    @JoinColumn(name = "editor_id", nullable = false)
    private Editor editor;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "book_id")
    private List<Image> images = new ArrayList<>();
}