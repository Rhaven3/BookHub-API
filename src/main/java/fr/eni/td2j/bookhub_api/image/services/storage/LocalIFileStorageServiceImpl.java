package fr.eni.td2j.bookhub_api.image.services.storage;

import fr.eni.td2j.bookhub_api.exception.BadRequestException;
import fr.eni.td2j.bookhub_api.exception.FileStorageException;
import fr.eni.td2j.bookhub_api.exception.NotFoundException;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Objects;

@Service
public class LocalIFileStorageServiceImpl implements IFileStorageService {
    
    @Value("${app.upload.dir}")
    private String uploadDir;

    /**
     *
     * Méthode en charge de stocker un fichier
     * @param file
     * @return Le nom du fichier stocké
     */
    @Override
    public String store(MultipartFile file) {
        validateFile(file);

        String filename = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));

        Path uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();
        Path target = uploadPath.resolve(filename).normalize();

        if (!target.getParent().equals(uploadPath)) {
            throw new BadRequestException("Chemin invalide");
        }

        try (InputStream is = file.getInputStream()) {
            Files.createDirectories(uploadPath);
            Files.copy(is, target, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new FileStorageException("Échec upload : " + e.getMessage(), e);
        }
        return filename;
    }

    /**
     *
     * Méthode en charge de charger un fichier
     * @param filename
     * @return Le fichier sous forme de Resource
     */
    @Override
    public Resource load(String filename) {
        try {
            Path file = Paths.get(uploadDir).resolve(filename).normalize();
            Resource resource = new UrlResource(file.toUri());
            if (!resource.exists()) throw new NotFoundException("Fichier introuvable");
            return resource;
        } catch (MalformedURLException e) {
            throw new FileStorageException("Erreur de lecture", e);
        }
    }

    /**
     *
     * Méthode en charge de supprimer un fichier
     * @param filename
     */
    @Override
    public void delete(String filename) {
        try {
            Path target = Paths.get(uploadDir).resolve(filename).normalize();
            Files.deleteIfExists(target);
        } catch (IOException e) {
            throw new FileStorageException("Échec de la suppression : " + e.getMessage(), e);
        }
    }

    /**
     *
     * Méthode en charge de valider un fichier
     * @param file
     */
    private void validateFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new BadRequestException("Fichier vide");
        }

        String contentType;
        try (InputStream is = file.getInputStream()) {
            contentType = new Tika().detect(is);
            System.out.println("DEBUG - Content-Type détecté : " + contentType);
        } catch (IOException e) {
            throw new FileStorageException("Impossible de lire le fichier", e);
        }

        List<String> allowedTypes = List.of(
                "image/jpeg",
                "image/png",
                "image/webp",
                "image/gif",
                "image/bmp",
                "image/tiff"
        );
        if (!allowedTypes.contains(contentType)) {
            throw new BadRequestException("Type de fichier non autorisé : " + contentType);
        }
    }
}