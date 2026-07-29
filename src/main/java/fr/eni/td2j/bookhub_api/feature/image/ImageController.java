package fr.eni.td2j.bookhub_api.feature.image;

import fr.eni.td2j.bookhub_api.common.ApiResponse;
import fr.eni.td2j.bookhub_api.feature.image.services.ImageService;
import fr.eni.td2j.bookhub_api.feature.image.services.storage.IFileStorageService;
import lombok.RequiredArgsConstructor;
import org.apache.tika.Tika;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/images")
public class ImageController {

    private final ImageService imageService;
    private final IFileStorageService fileStorageService;

    @PostMapping("/upload")
    public ResponseEntity<ApiResponse<Image>> upload(@RequestParam("file") MultipartFile file, @RequestParam("name") String name) {
        String storedFilename = fileStorageService.store(file);

        Image image = new Image();
        image.setName(name);
        image.setPath("./uploads/" + storedFilename);

        imageService.addImage(image);

        return ResponseEntity.ok().body(ApiResponse.success(image, "Image téléchargée avec succès"));
    }

    @GetMapping("/download/{filename}")
    public ResponseEntity<Resource> downloadImage(@PathVariable String filename) throws IOException {
        Resource resource = fileStorageService.load(filename);

        String contentType;
        try (InputStream is = resource.getInputStream()) {
            contentType = new Tika().detect(is);
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType != null ? contentType : "application/octet-stream"))
                .body(resource);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<Image>>> getImages(Pageable pageable) {
        return ResponseEntity.ok().body(ApiResponse.success(imageService.getImages(pageable), "Images récupérées avec succès"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Image>> getImage(@PathVariable Long id) {
        return ResponseEntity.ok().body(ApiResponse.success(imageService.getImage(id), "Image récupérée avec succès"));
    }

    @GetMapping("/nom/{name}")
    public ResponseEntity<ApiResponse<Image>> getImageByName(@PathVariable String name) {
        return ResponseEntity.ok().body(ApiResponse.success(imageService.getImageByName(name), "Image récupérée avec succès"));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<Image>>> getImageLikeName(
            @RequestParam String name,
            Pageable pageable) {

        if (name.isBlank()) {
            return ResponseEntity.ok(
                    ApiResponse.success(imageService.getImages(pageable), "Toutes les images")
            );
        }

        return ResponseEntity.ok(
                ApiResponse.success(imageService.getImageLikeName(name, pageable),"Images trouvées" )
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Image>> deleteImage(@PathVariable Long id) {
        imageService.deleteImage(id);
        return ResponseEntity.ok().body(ApiResponse.success(null, "Image supprimée avec succès"));
    }
}
