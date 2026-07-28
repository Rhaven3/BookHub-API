package fr.eni.td2j.bookhub_api.feature.reservation.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Classe 'ReservationRequestDTO' en charge de
 *
 * @author jnsualu2026
 * @since 2026-07-27
 */
@Getter
@Setter
@NoArgsConstructor
public class ReservationRequestDTO {

    @NotNull(message = "L'identifiant du livre est obligatoire.")
    private Long bookId;

}
