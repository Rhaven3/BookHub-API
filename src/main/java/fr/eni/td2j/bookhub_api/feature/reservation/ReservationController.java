package fr.eni.td2j.bookhub_api.feature.reservation;

import fr.eni.td2j.bookhub_api.common.ApiResponse;
import fr.eni.td2j.bookhub_api.common.dtos.page.PageResponseDTO;
import fr.eni.td2j.bookhub_api.common.dtos.page.mapper.PageMapper;
import fr.eni.td2j.bookhub_api.feature.reservation.dto.ReservationRequestDTO;
import fr.eni.td2j.bookhub_api.feature.reservation.dto.ReservationResponseDTO;
import fr.eni.td2j.bookhub_api.feature.reservation.emuns.ReservationEnum;
import fr.eni.td2j.bookhub_api.feature.user.User;
import fr.eni.td2j.bookhub_api.security.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

/**
 * Classe 'ReservationController' en charge de
 *
 * @author jnsualu2026
 * @since 2026-07-27
 */

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService service;
    private final PageMapper pageMapper;
    private static final String RESERVATIONS_FOUND = "Réservations récupérées avec succès.";


    @GetMapping("/me")
    public ResponseEntity<ApiResponse<PageResponseDTO<ReservationResponseDTO>>> getReservations(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            Pageable pageable) {

        User user = customUserDetails.getUser();

        Page<ReservationResponseDTO> reservations = service.getByUser(user, pageable);

        return ResponseEntity.ok(
                ApiResponse.success(
                        pageMapper.toDto(reservations),
                        RESERVATIONS_FOUND
                )
        );
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ReservationResponseDTO>> createReservation(
            @Valid @RequestBody ReservationRequestDTO requestDTO,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        new ApiResponse<>(
                                HttpStatus.CREATED.value(),
                                Instant.now(),
                                "Réservation créée avec succès.",
                                service.createReservation(
                                        requestDTO,
                                        customUserDetails.getUser()
                                )
                        )
                );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> cancelReservation(@PathVariable Long id) {
        service.cancelReservation(id);
        return ResponseEntity.ok(
                ApiResponse.success(null, "Réservation annulée avec succès.")
        );
    }

    @GetMapping("/status")
    public ResponseEntity<ApiResponse<PageResponseDTO<ReservationResponseDTO>>> getByStatus(
            @RequestParam ReservationEnum status,
            Pageable pageable) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        pageMapper.toDto(service.getByStatus(status, pageable)),
                        RESERVATIONS_FOUND
                )
        );
    }

    @GetMapping("/expirer/{bookId}")
    public ResponseEntity<ApiResponse<List<ReservationResponseDTO>>> getByReservationLimitDateBefore(
            @PathVariable Long bookId,
            @RequestParam Instant now) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        service.getByReservationLimitDateBefore(bookId, now),
                        RESERVATIONS_FOUND
                )
        );
    }

    @GetMapping("/book/{bookId}")
    public ResponseEntity<ApiResponse<PageResponseDTO<ReservationResponseDTO>>> getByBook(
            @PathVariable Long bookId,
            @RequestParam ReservationEnum status,
            Pageable pageable) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        pageMapper.toDto(service.getStatusByBook(bookId, status, pageable)),
                        RESERVATIONS_FOUND
                )
        );
    }
}
