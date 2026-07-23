package fr.eni.td2j.bookhub_api.loan;

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
public class Loan {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
    private LocalDateTime loanDate;
    private LocalDateTime expectedReturnDate;
    private LocalDateTime actualReturnDate;
    private LoanEnum status;

    /**
     * retourne le nombre de jours de retard si il est en retard, -1 si il n'y a pas de retard
     * @return int nbr de jour de retard
     */
    public Integer getDelay() {
        LocalDateTime now = LocalDateTime.now();
        if (now.isAfter(expectedReturnDate) && status == LoanEnum.IN_PROGRESS) {
            return expectedReturnDate.getDayOfYear() - now.getDayOfYear();
        }
        return -1;
    }
}
