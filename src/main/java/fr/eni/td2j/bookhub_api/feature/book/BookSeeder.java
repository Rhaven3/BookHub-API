package fr.eni.td2j.bookhub_api.feature.book;

import fr.eni.td2j.bookhub_api.feature.author.Author;
import fr.eni.td2j.bookhub_api.feature.author.AuthorRepository;
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

        Author orwell = authorRepository.findAll().stream()
                .filter(a -> a.getLname().equals("Orwell"))
                .findFirst()
                .orElseThrow();

        Author herbert = authorRepository.findAll().stream()
                .filter(a -> a.getLname().equals("Herbert"))
                .findFirst()
                .orElseThrow();

        Category dystopie = categoryRepository.findAll().stream()
                .filter(c -> c.getName().equals("Dystopie"))
                .findFirst()
                .orElseThrow();

        Category sf = categoryRepository.findAll().stream()
                .filter(c -> c.getName().equals("Science-Fiction"))
                .findFirst()
                .orElseThrow();

        Editor gallimard = editorRepository.findAll().stream()
                .filter(e -> e.getName().equals("Gallimard"))
                .findFirst()
                .orElseThrow();

        Image cover1984 = Image.builder()
                .name("1984-cover")
                .path("/images/books/1984-cover.jpg")
                .build();

        Image coverDune = Image.builder()
                .name("dune-cover")
                .path("/images/books/dune-cover.jpg")
                .build();

        Book book1984 = Book.builder()
                .title("1984")
                .description("Un roman dystopique sur la surveillance de masse.")
                .publishDate(LocalDate.of(1949, 6, 8))
                .language("Français")
                .isbn("978-2070368228")
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
                .isbn("978-2266334822")
                .available(true)
                .owned(false)
                .authors(List.of(herbert))
                .categories(List.of(sf))
                .editor(gallimard)
                .images(List.of(coverDune))
                .build();

        bookRepository.save(book1984);
        bookRepository.save(bookDune);
    }

    @Override
    public int order() {
        return 2; // après Author, Category, Editor, Image
    }
}