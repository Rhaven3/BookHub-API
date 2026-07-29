package fr.eni.td2j.bookhub_api.feature.author.dto;

import lombok.*;

/**
 * Classe 'AuthorResponseDTO' en charge de
 *
 * @author jnsualu2026
 * @since 2026-07-28
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthorResponseDTO {
    private Long id;
    private String fname;
    private String lname;
}
