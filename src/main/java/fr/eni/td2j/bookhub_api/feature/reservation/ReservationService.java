package fr.eni.td2j.bookhub_api.feature.reservation;

import fr.eni.td2j.bookhub_api.exception.BadRequestException;
import fr.eni.td2j.bookhub_api.exception.NotFoundException;
import fr.eni.td2j.bookhub_api.feature.book.Book;
import fr.eni.td2j.bookhub_api.feature.book.BookRepository;
import fr.eni.td2j.bookhub_api.feature.reservation.dto.ReservationRequestDTO;
import fr.eni.td2j.bookhub_api.feature.reservation.dto.ReservationResponseDTO;
import fr.eni.td2j.bookhub_api.feature.reservation.dto.mapper.ReservationMapper;
import fr.eni.td2j.bookhub_api.feature.reservation.emuns.ReservationEnum;
import fr.eni.td2j.bookhub_api.feature.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Classe 'ReservationService' en charge de
 *
 * @author jnsualu2026
 * @since 2026-07-27
 */
@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository repository;
    private final BookRepository  bookRepository;
    private final ReservationMapper reservationMapper;

    public ReservationResponseDTO createReservation(
            ReservationRequestDTO requestDTO,
            User user
    ) {

        Book book = bookRepository.findById(requestDTO.getBookId())
                .orElseThrow(() -> new NotFoundException("Livre non trouvé."));

        boolean alreadyReserved = !repository
                .findByUserAndBookAndStatusIn(
                        user,
                        book,
                        List.of(
                                ReservationEnum.WAITING,
                                ReservationEnum.AVAILABLE
                        )
                )
                .isEmpty();

        if (alreadyReserved) {
            throw new BadRequestException(
                    "Il y a déjà une réservation active sur ce livre."
            );
        }

        boolean bookWasAvailable = book.isAvailable();

        Reservation newReservation = Reservation.builder()
                .user(user)
                .book(book)
                .registrationDate(Instant.now())
                .reservationLimitDate(
                        Instant.now().plus(30, ChronoUnit.DAYS)
                )
                .status(
                        bookWasAvailable
                                ? ReservationEnum.AVAILABLE
                                : ReservationEnum.WAITING
                )
                .build();


        if (bookWasAvailable) {
            book.setAvailable(false);
            bookRepository.save(book);
        }

        Reservation savedReservation = repository.save(newReservation);

        return reservationMapper.toDto(savedReservation);
    }

    /**
     * Méthode en charge de récupérer, de manière paginée, les réservations
     * d'un livre donné ayant le statut fourni (ex: consulter la file d'attente
     * d'un livre en filtrant sur le statut WAITING).
     * @param id identifiant du livre concerné par la recherche
     * @param status statut recherché parmi les réservations de ce livre
     * @param pageable informations de pagination (numéro de page, taille)
     * @return une page de réservations du livre ayant le statut donné
     */
    public Page<ReservationResponseDTO> getStatusByBook(Long id, ReservationEnum status, Pageable pageable) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Livre non trouvé."));
        return repository.findByBookAndStatus(book, status, pageable).map(reservationMapper::toDto);
    }

    /**
     * Méthode en charge d'annuler une réservation. Si la réservation annulée
     * avait le statut AVAILABLE (livre mis de côté pour l'utilisateur), la file
     * d'attente est réévaluée : la prochaine réservation WAITING (la plus ancienne)
     * passe à AVAILABLE, ou le livre redevient disponible s'il n'y a plus d'attente.
     * @param id identifiant de la réservation à annuler
     * @throws NotFoundException si la réservation n'existe pas
     */
    public void cancelReservation(Long id) {
        Reservation reservation = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Réservation non trouvée."));

        boolean wasAvailable = reservation.getStatus() == ReservationEnum.AVAILABLE;

        reservation.setStatus(ReservationEnum.CANCELED);
        repository.save(reservation);

        if (wasAvailable) {
            promoteNextUserInQueue(reservation.getBook());
        }
    }

    /**
     *
     * Méthode en charge de promouvoir le proschain utilisateur de la file d'attente ou si y'a personne le rendre disponible
     * @param book le livre dont la réservation est à gérer
     */
    private void promoteNextUserInQueue(Book book) {
        List<Reservation> waitingList = repository.findByBookAndStatus(
                book, ReservationEnum.WAITING, Pageable.ofSize(1)).getContent();
        if (waitingList.isEmpty()) {
            book.setAvailable(true);
            bookRepository.save(book);
        } else {
            Reservation next = waitingList.get(0);
            next.setStatus(ReservationEnum.AVAILABLE);
            repository.save(next);
        }
    }


    public List<ReservationResponseDTO> getByReservationLimitDateBefore(Long bookId, Instant now) {
        Book book = repository.findById(bookId)
                .orElseThrow(() -> new NotFoundException("Livre non trouvé.")).getBook();
        return repository.findByBookAndReservationLimitDateBefore(book, now).stream()
                .map(reservationMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Méthode en charge de récupérer, de manière paginée, les réservations
     * ayant le statut fourni.
     * @param status statut recherché parmi les réservations
     * @param pageable informations de pagination (numéro de page, taille)
     * @return une page de réservations ayant le statut donné
     */
    public Page<ReservationResponseDTO> getByStatus(ReservationEnum status, Pageable pageable) {
        return repository.findByStatus(status, pageable).map(reservationMapper::toDto);
    }

    /**
     * Méthode en charge de récupérer, de manière paginée, les réservations
     * appartenant à l'utilisateur fourni.
     * @param user utilisateur dont on souhaite consulter les réservations
     * @param pageable informations de pagination (numéro de page, taille)
     * @return une page de réservations appartenant à l'utilisateur donné
     */
    public Page<ReservationResponseDTO> getByUser(User user, Pageable pageable) {
        return repository.findByUser(user, pageable)
                .map(reservationMapper::toDto);
    }

    public List<ReservationResponseDTO> getByBook(Book book) {
        return repository.findByBook(book).stream().map(reservationMapper::toDto).collect(Collectors.toList());
    }

    public List<ReservationResponseDTO> getByBookAndStatus(Book book, ReservationEnum status) {
        return repository.findByBookAndStatus(book, status).stream().map(reservationMapper::toDto).collect(Collectors.toList());
    }
}
