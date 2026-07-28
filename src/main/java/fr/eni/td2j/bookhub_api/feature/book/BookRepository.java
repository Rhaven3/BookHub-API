package fr.eni.td2j.bookhub_api.feature.book;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {
    Optional<Book> findByTitleIgnoreCase(String title);
    Optional<Book> findByIsbn(String isbn);
    Page<Book> findByAvailable(boolean available, Pageable pageable);
    Page<Book> findByAuthorsId(Long authorId, Pageable pageable);
    Page<Book> findByCategoriesId(Long categoryId, Pageable pageable);
    Page<Book> findByEditorId(Long editorId, Pageable pageable);

    @Query("""
    SELECT DISTINCT b FROM Book b
    LEFT JOIN b.authors a
    WHERE (:title IS NULL OR LOWER(b.title) LIKE LOWER(CONCAT('%', :title, '%')))
    AND (:available IS NULL OR b.available = :available)
    AND (:authorName IS NULL OR LOWER(a.lname) LIKE LOWER(CONCAT('%', :authorName, '%')))
    """)
    Page<Book> search(
            @Param("title") String title,
            @Param("available") Boolean available,
            @Param("authorName") String authorName,
            Pageable pageable
    );
}
