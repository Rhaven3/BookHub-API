package fr.eni.td2j.bookhub_api.reservation;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Entity
public class Reservation {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
    private LocalDateTime registrationDate;
    private LocalDateTime reservationLimitDate;
    private ReservationEnum status;


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
//    }
}
