package fr.eni.td2j.bookhub_api.image;

import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ImageRepository extends JpaRepository<Image, Long> {
    Optional<Image> findByName(String name);
    void deleteById(@NonNull Long id);
    boolean existsByName(String name);
    @Query("""
    SELECT i 
    FROM Image i 
    WHERE LOWER(i.name) LIKE LOWER(CONCAT('%', :name, '%'))
""")
    Page<Image> findByNameLike(
            @Param("name") String name,
            Pageable pageable
    );
}


