package fr.eni.td2j.bookhub_api.editor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EditorServiceImpl implements EditorService {

    private final EditorRepository editorRepository;

    public EditorServiceImpl(EditorRepository editorRepository) {
        this.editorRepository = editorRepository;
    }

    @Override
    public Page<Editor> findAll(Pageable pageable) {
        return editorRepository.findAll(pageable);
    }

    @Override
    public Optional<Editor> findById(long id) {
        return editorRepository.findById(id);
    }

    @Override
    public Editor create(Editor editor) {
        if (editor.getId() != null) {
            throw new IllegalArgumentException("L'id doit être null.");
        }

        if (editorRepository.existsByNameIgnoreCase(editor.getName())) {
            throw new IllegalArgumentException("Cet éditeur existe déjà.");
        }
        return editorRepository.save(editor);
    }

    @Override
    public Editor update(Editor editor) {
        if (!editorRepository.existsById(editor.getId())) {
            throw new IllegalArgumentException("Éditeur introuvable.");
        }
        return editorRepository.save(editor);
    }

    @Override
    public void delete(long id) {
        if (!editorRepository.existsById(id)) {
            throw new IllegalArgumentException("Éditeur introuvable.");
        }
        editorRepository.deleteById(id);
    }
}
