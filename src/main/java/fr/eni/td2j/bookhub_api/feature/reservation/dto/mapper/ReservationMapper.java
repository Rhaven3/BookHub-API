package fr.eni.td2j.bookhub_api.feature.reservation.dto.mapper;

import fr.eni.td2j.bookhub_api.common.EntityMapper;
import fr.eni.td2j.bookhub_api.feature.book.dto.mapper.BookMapper;
import fr.eni.td2j.bookhub_api.feature.reservation.Reservation;
import fr.eni.td2j.bookhub_api.feature.reservation.dto.ReservationResponseDTO;
import fr.eni.td2j.bookhub_api.feature.user.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Classe 'ReservationMapper' en charge de
 *
 * @author jnsualu2026
 * @since 2026-07-27
 */

@Component
@RequiredArgsConstructor
public class ReservationMapper implements EntityMapper<Reservation, ReservationResponseDTO> {

    private final UserMapper userMapper;
    private final BookMapper bookMapper;

    public ReservationResponseDTO toDto(Reservation reservation) {

        return ReservationResponseDTO.builder()
                .id(reservation.getId())
                .registrationDate(reservation.getRegistrationDate())
                .reservationLimitDate(reservation.getReservationLimitDate())
                .status(reservation.getStatus())
                .user(
                        reservation.getUser() != null
                                ? userMapper.toDto(reservation.getUser())
                                : null
                )
                .book(
                        reservation.getBook() != null
                                ? bookMapper.toDto(reservation.getBook())
                                : null
                )
                .build();
    }
}