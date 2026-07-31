package fr.eni.td2j.bookhub_api.feature.editor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/editor")
public class EditorController {
    private final EditorService editorService;

    public EditorController(EditorService editorService) {
        this.editorService = editorService;
    }

    @GetMapping
    public ResponseEntity<Page<Editor>> findAll(Pageable pageable) {
        return ResponseEntity.ok(editorService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Editor> findById(@PathVariable Long id) {
        return editorService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Editor> create(@RequestBody Editor editor) {
        Editor created = editorService.create(editor);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Editor> update(@PathVariable Long id, @RequestBody Editor editor) {
        Editor updated = editorService.update(editor);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        editorService.delete(id);
        return ResponseEntity.noContent().build();
    }

    }
