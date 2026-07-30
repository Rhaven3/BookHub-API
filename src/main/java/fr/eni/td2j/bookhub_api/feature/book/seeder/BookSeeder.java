package fr.eni.td2j.bookhub_api.feature.book.seeder;

import fr.eni.td2j.bookhub_api.feature.author.Author;
import fr.eni.td2j.bookhub_api.feature.author.AuthorRepository;
import fr.eni.td2j.bookhub_api.feature.book.Book;
import fr.eni.td2j.bookhub_api.feature.book.BookRepository;
import fr.eni.td2j.bookhub_api.feature.category.Category;
import fr.eni.td2j.bookhub_api.feature.category.CategoryRepository;
import fr.eni.td2j.bookhub_api.feature.editor.Editor;
import fr.eni.td2j.bookhub_api.feature.editor.EditorRepository;
import fr.eni.td2j.bookhub_api.feature.image.Image;
import fr.eni.td2j.bookhub_api.feature.image.ImageRepository;
import fr.eni.td2j.bookhub_api.common.seeder.EntitySeeder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class BookSeeder implements EntitySeeder {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final CategoryRepository categoryRepository;
    private final EditorRepository editorRepository;
    private final ImageRepository imageRepository;

    public BookSeeder(BookRepository bookRepository,
                      AuthorRepository authorRepository,
                      CategoryRepository categoryRepository,
                      EditorRepository editorRepository,
                      ImageRepository imageRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.categoryRepository = categoryRepository;
        this.editorRepository = editorRepository;
        this.imageRepository = imageRepository;
    }

    @Override
    public void seed() {
        if (bookRepository.count() > 0) return;

        // ---------- Auteurs ----------
        Author orwell = authorRepository.findAll().stream()
                .filter(a -> a.getLastName().equals("Orwell"))
                .findFirst()
                .orElseThrow();

        Author herbert = authorRepository.findAll().stream()
                .filter(a -> a.getLastName().equals("Herbert"))
                .findFirst()
                .orElseThrow();

        Author asimov = authorRepository.findAll().stream()
                .filter(a -> a.getLastName().equals("Asimov"))
                .findFirst()
                .orElseThrow();

        Author tolkien = authorRepository.findAll().stream()
                .filter(a -> a.getLastName().equals("Tolkien"))
                .findFirst()
                .orElseThrow();

        Author rowling = authorRepository.findAll().stream()
                .filter(a -> a.getLastName().equals("Rowling"))
                .findFirst()
                .orElseThrow();

        Author saintExupery = authorRepository.findAll().stream()
                .filter(a -> a.getLastName().equals("de Saint-Exupéry"))
                .findFirst()
                .orElseThrow();

        // ---------- Catégories ----------
        Category dystopie = categoryRepository.findAll().stream()
                .filter(c -> c.getName().equals("Dystopie"))
                .findFirst()
                .orElseThrow();

        Category sf = categoryRepository.findAll().stream()
                .filter(c -> c.getName().equals("Science-Fiction"))
                .findFirst()
                .orElseThrow();

        Category fantasy =categoryRepository.findAll().stream()
                .filter(c -> c.getName().equals("Fantasy"))
                .findFirst()
                .orElseThrow();

        Category classique = categoryRepository.findAll().stream()
                .filter(c -> c.getName().equals("Roman"))
                .findFirst()
                .orElseThrow();

        // ---------- Éditeurs ----------
        Editor gallimard = editorRepository.findAll().stream()
                .filter(e -> e.getName().equals("Gallimard"))
                .findFirst()
                .orElseThrow();

        Editor folio = editorRepository.findAll().stream()
                .filter(e -> e.getName().equals("Folio"))
                .findFirst()
                .orElseThrow();

        Editor pocket = editorRepository.findAll().stream()
                .filter(e -> e.getName().equals("Pocket"))
                .findFirst()
                .orElseThrow();

        // ---------- Images ----------
        Image cover1984 = Image.builder()
                .name("1984-cover")
                .path("./uploads/fleur.jpg")
                .build();

        Image coverDune = Image.builder()
                .name("dune-cover")
                .path("./uploads/fleur.jpg")
                .build();

        Image coverFondation = Image.builder()
                .name("fondation-cover")
                .path("./uploads/fleur.jpg")
                .build();

        Image coverHobbit = Image.builder()
                .name("hobbit-cover")
                .path("./uploads/fleur.jpg")
                .build();

        Image coverHarryPotter = Image.builder()
                .name("harry-potter-cover")
                .path("./uploads/fleur.jpg")
                .build();

        Image coverPetitPrince = Image.builder()
                .name("petit-prince-cover")
                .path("./uploads/fleur.jpg")
                .build();

        Book book1984 = Book.builder()
                .title("1984")
                .description("Un roman dystopique sur la surveillance de masse.")
                .publishDate(LocalDate.of(1949, 6, 8))
                .language("Français")
                .isbn("9782070368228")
                .available(true)
                .owned(true)
                .authors(List.of(orwell))
                .categories(List.of(dystopie))
                .editor(gallimard)
                .images(List.of(cover1984))
                .build();

        Book bookDune = Book.builder()
                .title("Dune")
                .description("L'histoire de Paul Atréides sur la planète Arrakis.")
                .publishDate(LocalDate.of(1965, 8, 1))
                .language("Français")
                .isbn("9782266334822")
                .available(true)
                .owned(false)
                .authors(List.of(herbert))
                .categories(List.of(sf))
                .editor(gallimard)
                .images(List.of(coverDune))
                .build();

        Book bookFondation = Book.builder()
                .title("Fondation")
                .description("Le premier tome de la célèbre saga imaginée par Isaac Asimov.")
                .publishDate(LocalDate.of(1951, 6, 1))
                .language("Français")
                .isbn("9782070360536")
                .available(true)
                .owned(true)
                .authors(List.of(asimov))
                .categories(List.of(sf))
                .editor(folio)
                .images(List.of(coverFondation))
                .build();

        Book bookHobbit = Book.builder()
                .title("Le Hobbit")
                .description("Les aventures de Bilbon Sacquet avant les événements du Seigneur des Anneaux.")
                .publishDate(LocalDate.of(1937, 9, 21))
                .language("Français")
                .isbn("9782266283038")
                .available(false)
                .owned(true)
                .authors(List.of(tolkien))
                .categories(List.of(fantasy))
                .editor(pocket)
                .images(List.of(coverHobbit))
                .build();

        Book bookHarryPotter = Book.builder()
                .title("Harry Potter à l'école des sorciers")
                .description("Le premier tome des aventures du jeune sorcier Harry Potter.")
                .publishDate(LocalDate.of(1997, 6, 26))
                .language("Français")
                .isbn("9782070643028")
                .available(true)
                .owned(false)
                .authors(List.of(rowling))
                .categories(List.of(fantasy))
                .editor(gallimard)
                .images(List.of(coverHarryPotter))
                .build();

        Book bookLePetitPrince = Book.builder()
                .title("Le Petit Prince")
                .description("Un conte poétique et philosophique d'Antoine de Saint-Exupéry.")
                .publishDate(LocalDate.of(1943, 4, 6))
                .language("Français")
                .isbn("9782070408504")
                .available(true)
                .owned(true)
                .authors(List.of(saintExupery))
                .categories(List.of(classique))
                .editor(folio)
                .images(List.of(coverPetitPrince))
                .build();

        bookRepository.saveAll(List.of(
                book1984,
                bookDune,
                bookFondation,
                bookHobbit,
                bookHarryPotter,
                bookLePetitPrince
        ));
    }

    @Override
    public int order() {
        return 2; // après Author, Category, Editor, Image
    }
}