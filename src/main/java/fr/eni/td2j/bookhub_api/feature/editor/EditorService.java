package fr.eni.td2j.bookhub_api.feature.editor;

import fr.eni.td2j.bookhub_api.exception.BadRequestException;
import fr.eni.td2j.bookhub_api.exception.NotFoundException;
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
            throw new BadRequestException("L'id doit être null.");
        }

        if (editorRepository.existsByNameIgnoreCase(editor.getName())) {
            throw new BadRequestException("Cet éditeur existe déjà.");
        }
        return editorRepository.save(editor);
    }

    public Editor update(Editor editor) {
        Editor existingEditor = findById(editor.getId())
                .orElseThrow(() -> new NotFoundException("Editeur introuvable"));

        if (!editorRepository.existsById(editor.getId())) {
            throw new NotFoundException("Éditeur introuvable.");
        }

        if (editorRepository.existsByNameIgnoreCase(editor.getName())) {
            throw new BadRequestException("Cet editeur existe.");
        }

        existingEditor.setName(editor.getName());
        return editorRepository.save(existingEditor);
    }

    public void delete(Long id) {
        if (!editorRepository.existsById(id)) {
            throw new IllegalArgumentException("Éditeur introuvable.");
        }
        editorRepository.deleteById(id);
    }
}
