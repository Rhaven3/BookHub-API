package fr.eni.td2j.bookhub_api.feature.author;

import fr.eni.td2j.bookhub_api.common.seeder.EntitySeeder;
import org.springframework.stereotype.Component;

@Component
public class AuthorSeeder implements EntitySeeder {

    private final AuthorRepository authorRepository;

    public AuthorSeeder(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Override
    public void seed() {
        if (authorRepository.count() > 0) return;

        authorRepository.save(Author.builder().fname("George").lname("Orwell").build());
        authorRepository.save(Author.builder().fname("J.K.").lname("Rowling").build());
        authorRepository.save(Author.builder().fname("Frank").lname("Herbert").build());
        authorRepository.save(Author.builder().fname("Isaac").lname("Asimov").build());
        authorRepository.save(Author.builder().fname("Antoine").lname("de Saint-Exupéry").build());
        authorRepository.save(Author.builder().fname("J.R.R").lname("Tolkien").build());

    }

    @Override
    public int order() {
        return 1;
    }
}
