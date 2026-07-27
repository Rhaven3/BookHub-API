package fr.eni.td2j.bookhub_api.feature.category;

import fr.eni.td2j.bookhub_api.seeder.EntitySeeder;
import org.springframework.stereotype.Component;

@Component
public class CategorySeeder implements EntitySeeder {

    private final CategoryRepository categoryRepository;

    public CategorySeeder(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public void seed() {
        if (categoryRepository.count() > 0) return;

        categoryRepository.save(Category.builder().name("Science-Fiction").build());
        categoryRepository.save(Category.builder().name("Fantasy").build());
        categoryRepository.save(Category.builder().name("Dystopie").build());
        categoryRepository.save(Category.builder().name("Roman").build());
    }

    @Override
    public int order() {
        return 1;
    }
}
