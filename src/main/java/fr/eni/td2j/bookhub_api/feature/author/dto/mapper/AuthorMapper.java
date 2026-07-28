package fr.eni.td2j.bookhub_api.feature.author.dto.mapper;

import fr.eni.td2j.bookhub_api.feature.author.Author;
import fr.eni.td2j.bookhub_api.feature.author.dto.AuthorResponseDTO;
import org.springframework.stereotype.Component;

/**
 * Classe 'AuthorMapper' en charge de convertir
 * une entité Author en AuthorResponseDTO.
 *
 * @author jnsualu2026
 * @since 2026-07-28
 */
@Component
public class AuthorMapper {

    public AuthorResponseDTO toDto(Author author) {
        if (author == null) {
            return null;
        }

        return AuthorResponseDTO.builder()
                .id(author.getId())
                .fname(author.getFname())
                .lname(author.getLname())
                .build();
    }
}