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
    SELECT DISTINCT b
    FROM Book b
    LEFT JOIN b.authors a
    LEFT JOIN b.categories c
    WHERE (:word IS NULL
        OR LOWER(b.title) LIKE LOWER(CONCAT('%', :word, '%'))
        OR LOWER(a.lname) LIKE LOWER(CONCAT('%', :word, '%'))
        OR LOWER(c.name) LIKE LOWER(CONCAT('%', :word, '%'))
        OR LOWER(b.editor.name) LIKE LOWER(CONCAT('%', :word, '%'))
        OR LOWER(b.isbn) LIKE LOWER(CONCAT('%', :word, '%'))
    )
    """)
    Page<Book> search(
            @Param("word") String word,
            Pageable pageable
    );


    @Query("""
SELECT DISTINCT b
FROM Book b
LEFT JOIN b.authors a
LEFT JOIN b.categories c
WHERE
    (:word IS NULL
        OR LOWER(b.title) LIKE LOWER(CONCAT('%', :word, '%'))
        OR LOWER(a.lname) LIKE LOWER(CONCAT('%', :word, '%'))
        OR LOWER(c.name) LIKE LOWER(CONCAT('%', :word, '%'))
        OR LOWER(b.editor.name) LIKE LOWER(CONCAT('%', :word, '%'))
        OR LOWER(b.isbn) LIKE LOWER(CONCAT('%', :word, '%'))
    )
AND (:authorId IS NULL OR a.id = :authorId)
AND (:categoryId IS NULL OR c.id = :categoryId)
AND (:editorId IS NULL OR b.editor.id = :editorId)
""")
    Page<Book> filter(
            @Param("word") String word,
            @Param("authorId") Long authorId,
            @Param("categoryId") Long categoryId,
            @Param("editorId") Long editorId,
            Pageable pageable
    );
}
