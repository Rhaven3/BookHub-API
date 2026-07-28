package fr.eni.td2j.bookhub_api.feature.reservation;

import aj.org.objectweb.asm.commons.Remapper;
import fr.eni.td2j.bookhub_api.feature.book.Book;
import fr.eni.td2j.bookhub_api.feature.loan.Loan;
import fr.eni.td2j.bookhub_api.feature.reservation.emuns.ReservationEnum;
import fr.eni.td2j.bookhub_api.feature.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.Instant;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByUserAndBookAndStatusIn(User user, Book book, List<ReservationEnum> statuses); // check anti-doublon à la création
    List<Reservation> findByBookAndReservationLimitDateBefore(Book book, Instant now); // trouver les réservations expirées (job de nettoyage)
    List<Reservation> findByBook(Book book);
    List<Reservation> findByBookAndStatus(Book book, ReservationEnum status); // ex: qui est en attente pour ce livre précis (file d'attente)
    Page<Reservation> findByStatus(ReservationEnum status, Pageable pageable); // consultation admin, toutes réservations par statut
    Page<Reservation> findByUser(User user, Pageable pageable); // historique des réservations d'un utilisateur
    Page<Reservation> findByBookAndStatus(Book book, ReservationEnum status, Pageable pageable); // ex: qui est en attente pour ce livre précis (file d'attente)

}
