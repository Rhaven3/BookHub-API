package fr.eni.td2j.bookhub_api.editor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface EditorService {
    Page<Editor> findAll(Pageable pageable);
    Optional<Editor> findById(long id);
    Editor create(Editor editor);
    Editor update(Editor editor);
    void delete(long id);
}
