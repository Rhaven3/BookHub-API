package fr.eni.td2j.bookhub_api.image;

import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ImageRepository extends JpaRepository<Image, Long> {
    Optional<Image> findByName(String name);
    void deleteById(@NonNull Long id);
    boolean existsByName(String name);
}

