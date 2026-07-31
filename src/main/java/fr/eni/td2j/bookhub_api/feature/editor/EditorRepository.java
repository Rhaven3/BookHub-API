package fr.eni.td2j.bookhub_api.feature.editor;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EditorRepository extends JpaRepository<Editor, Long> {
    boolean existsByNameIgnoreCase(String name);
}
