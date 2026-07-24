package fr.eni.td2j.bookhub_api.image.services;

import fr.eni.td2j.bookhub_api.exception.BadRequestException;
import fr.eni.td2j.bookhub_api.exception.NotFoundException;
import fr.eni.td2j.bookhub_api.image.Image;
import fr.eni.td2j.bookhub_api.image.ImageRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ImageService {

    private final ImageRepository repository;

    public Page<Image> getImages(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Image getImage(Long id) {
        return repository.findById(id).orElseThrow(() -> new NotFoundException("Image non trouvé."));
    }

    public Image getImageByName(String name) {
        return repository.findByName(name).orElseThrow(() -> new NotFoundException("Image non trouvé."));
    }

    public void addImage(Image image) {
        if (repository.existsByName(image.getName())) {
            throw new BadRequestException("Une image avec ce nom existe déjà");
        }

        repository.save(image);
    }

    public void deleteImage(Long id) {
        repository.findById(id).orElseThrow(() -> new NotFoundException("Image non trouvé."));
        repository.deleteById(id);
    }

    public boolean isExisting(String name) {

        if (repository.existsByName(name)) {
            throw new BadRequestException("Une image avec ce nom existe déjà");
        }

        return repository.existsByName(name);
    }
}
