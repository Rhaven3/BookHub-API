package fr.eni.td2j.bookhub_api.feature.image.services.storage;

import fr.eni.td2j.bookhub_api.exception.BadRequestException;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface IFileStorageService {
    // Rôle : écrire le fichier reçu (venant de la requête HTTP) sur le disque.
    String store(MultipartFile file) throws BadRequestException;
    // Rôle : lire un fichier déjà stocké, pour le renvoyer au client (affichage d'une image, téléchargement)
    Resource load(String filename);
    // Rôle : supprimer un fichier déjà stocké
    void delete(String filename);
}
