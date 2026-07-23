package fr.eni.td2j.bookhub_api.image;

import fr.eni.td2j.bookhub_api.common.ApiResponse;
import fr.eni.td2j.bookhub_api.image.services.ImageService;
import fr.eni.td2j.bookhub_api.image.services.storage.IFileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/images")
public class ImageController {

    private final ImageService imageService;
    private final IFileStorageService fileStorageService;

    @PostMapping("/upload")
    public ResponseEntity<ApiResponse<Image>> upload(@RequestParam("file") MultipartFile file) {
        String storedFilename = fileStorageService.store(file);

        Image image = new Image();
        image.setName(storedFilename);
        image.setPath("./uploads/" + storedFilename);

        imageService.addImage(image);

        return ResponseEntity.ok().body(ApiResponse.of(HttpStatus.OK.value(), "Image téléchargée avec succès", image));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<Image>>> getImages(Pageable pageable) {
        return ResponseEntity.ok().body(ApiResponse.of(HttpStatus.OK.value(), "Images récupérées avec succès", imageService.getImages(pageable)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Image>> getImage(@PathVariable Long id) {
        return ResponseEntity.ok().body(ApiResponse.of(HttpStatus.OK.value(), "Image récupérée avec succès", imageService.getImage(id)));
    }

    @GetMapping("/nom/{name}")
    public ResponseEntity<ApiResponse<Image>> getImageByName(@PathVariable String name) {
        return ResponseEntity.ok().body(ApiResponse.of(HttpStatus.OK.value(), "Image récupérée avec succès", imageService.getImageByName(name)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Image>> deleteImage(@PathVariable Long id) {
        imageService.deleteImage(id);
        return ResponseEntity.ok().body(ApiResponse.of(HttpStatus.OK.value(), "Image supprimée avec succès", null));
    }
}
