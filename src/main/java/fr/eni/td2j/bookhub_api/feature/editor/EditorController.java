package fr.eni.td2j.bookhub_api.feature.editor;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/editor")
public class EditorController {
    private final EditorService editorService;

    public EditorController(EditorService editorService) {
        this.editorService = editorService;
    }
}
