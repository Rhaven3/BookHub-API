package fr.eni.td2j.bookhub_api.feature.loan;

import fr.eni.td2j.bookhub_api.common.BaseEntity;
import fr.eni.td2j.bookhub_api.feature.book.Book;
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
public class Loan extends BaseEntity {
    private LocalDateTime loanDate;
    private LocalDateTime expectedReturnDate;
    private LocalDateTime actualReturnDate;
    private LoanEnum status;
    @ManyToOne
    private User user;
    @ManyToOne
    private Book book;

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
