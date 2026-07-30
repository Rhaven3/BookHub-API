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

        authorRepository.save(Author.builder().firstName("George").lastName("Orwell").build());
        authorRepository.save(Author.builder().firstName("J.K.").lastName("Rowling").build());
        authorRepository.save(Author.builder().firstName("Frank").lastName("Herbert").build());
        authorRepository.save(Author.builder().firstName("Isaac").lastName("Asimov").build());
    }

    @Override
    public int order() {
        return 1;
    }
}
