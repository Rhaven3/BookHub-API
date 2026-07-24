package fr.eni.td2j.bookhub_api.feature.reservation;

import fr.eni.td2j.bookhub_api.common.BaseEntity;
import fr.eni.td2j.bookhub_api.feature.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Entity
public class Reservation extends BaseEntity {
    private LocalDateTime registrationDate;
    private LocalDateTime reservationLimitDate;
    private ReservationEnum status;
    @ManyToOne
    private User user;


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
