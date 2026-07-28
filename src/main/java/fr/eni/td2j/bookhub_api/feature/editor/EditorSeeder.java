package fr.eni.td2j.bookhub_api.feature.editor;

import fr.eni.td2j.bookhub_api.seeder.EntitySeeder;
import org.springframework.stereotype.Component;

@Component
public class EditorSeeder implements EntitySeeder {

    private final EditorRepository editorRepository;

    public EditorSeeder(EditorRepository editorRepository) {
        this.editorRepository = editorRepository;
    }

    @Override
    public void seed() {
        if (editorRepository.count() > 0) return;

        editorRepository.save(Editor.builder().name("Gallimard").build());
        editorRepository.save(Editor.builder().name("Penguin Books").build());
        editorRepository.save(Editor.builder().name("Bragelonne").build());
    }

    @Override
    public int order() {
        return 1;
    }
}
