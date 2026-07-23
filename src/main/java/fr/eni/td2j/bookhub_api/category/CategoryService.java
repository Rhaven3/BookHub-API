package fr.eni.td2j.bookhub_api.category;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface CategoryService {
    Page<Category> findAll(Pageable pageable);
    Optional<Category> findById(Long id);
    Category create(Category category);
    Category update(Long id, Category category);
    void delete(Long id);
}
