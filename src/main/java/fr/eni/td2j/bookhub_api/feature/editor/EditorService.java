package fr.eni.td2j.bookhub_api.feature.editor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EditorService {

    private final EditorRepository editorRepository;

    public EditorService(EditorRepository editorRepository) {
        this.editorRepository = editorRepository;
    }

    public Page<Editor> findAll(Pageable pageable) {
        return editorRepository.findAll(pageable);
    }

    public Optional<Editor> findById(long id) {
        return editorRepository.findById(id);
    }

    public Editor create(Editor editor) {
        if (editor.getId() != null) {
            throw new IllegalArgumentException("L'id doit être null.");
        }

        if (editorRepository.existsByNameIgnoreCase(editor.getName())) {
            throw new IllegalArgumentException("Cet éditeur existe déjà.");
        }
        return editorRepository.save(editor);
    }

    public Editor update(Editor editor) {
        if (!editorRepository.existsById(editor.getId())) {
            throw new IllegalArgumentException("Éditeur introuvable.");
        }
        return editorRepository.save(editor);
    }

    public void delete(long id) {
        if (!editorRepository.existsById(id)) {
            throw new IllegalArgumentException("Éditeur introuvable.");
        }
        editorRepository.deleteById(id);
    }
}
