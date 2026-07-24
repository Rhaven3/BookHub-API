package fr.eni.td2j.bookhub_api.editor;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EditorRepository extends JpaRepository<Editor, Long> {
    boolean existsByNameIgnoreCase(String name);
}
