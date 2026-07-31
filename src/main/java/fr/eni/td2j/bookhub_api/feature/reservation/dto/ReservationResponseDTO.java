package fr.eni.td2j.bookhub_api.feature.reservation.dto;

import fr.eni.td2j.bookhub_api.feature.book.dto.BookResponseDTO;
import fr.eni.td2j.bookhub_api.feature.reservation.emuns.ReservationEnum;
import fr.eni.td2j.bookhub_api.feature.user.dto.response.UserResponseDTO;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.Instant;

/**
 * Classe 'ReservationDTO' en charge de
 *
 * @author jnsualu2026
 * @since 2026-07-27
 */

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ReservationResponseDTO {
    private Long id;
    private Instant registrationDate;
    private Instant reservationLimitDate;
    private ReservationEnum status;
    private UserResponseDTO user;
    private BookResponseDTO book;
}
