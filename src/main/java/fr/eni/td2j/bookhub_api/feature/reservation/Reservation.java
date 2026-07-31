package fr.eni.td2j.bookhub_api.feature.reservation;

import fr.eni.td2j.bookhub_api.common.BaseEntity;
import fr.eni.td2j.bookhub_api.feature.book.Book;
import fr.eni.td2j.bookhub_api.feature.reservation.emuns.ReservationEnum;
import fr.eni.td2j.bookhub_api.feature.user.User;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.Instant;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@ToString
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Entity
public class Reservation extends BaseEntity {
    private Instant registrationDate;
    private Instant reservationLimitDate;
    private ReservationEnum status;
    @ManyToOne
    private User user;

    @ManyToOne
    private Book book;


//    /**
//     * retourne la Position dans la file d'attente, -1 si la Reservation est annulé ou 1 si dispo
//     * @return int position dans la file d'attente
//     */
//    public Integer getPosition() {
//        if (status == ReservationEnum.CANCELED) {
//            return -1;
//        }
//        if (status == ReservationEnum.AVAILABLE) {
//            return 1;
//        }
//        return 0;
//    }
}
