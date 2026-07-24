package fr.eni.td2j.bookhub_api.feature.category;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Page<Category> findAll(Pageable pageable) {
        return categoryRepository.findAll(pageable);
    }

    public Optional<Category> findById(Long id) {
        return categoryRepository.findById(id);
    }

    public Category create(Category category) {
        if (category.getId() != null) {
            throw new IllegalStateException("Category already exists");
        }

        if (categoryRepository.existsByNameIgnoreCase(category.getName())) {
            throw new IllegalStateException("Category already exists");
        }
        return categoryRepository.save(category);

    }



    public Category update(Long id, Category category) {
        if(!categoryRepository.existsById(id)) {
            throw new IllegalStateException("Categorie introuvable");
        }
        category.setId(id);
        return categoryRepository.save(category);
    }

    public void delete(Long id) {
        if(!categoryRepository.existsById(id)) {
            throw new IllegalStateException("Categorie introuvable");
        }
        categoryRepository.deleteById(id);
    }
}
