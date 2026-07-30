package fr.eni.td2j.bookhub_api.feature.loan;

import fr.eni.td2j.bookhub_api.feature.book.Book;
import fr.eni.td2j.bookhub_api.feature.loan.dto.LoanResponseDTO;
import fr.eni.td2j.bookhub_api.feature.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface LoanRepository extends JpaRepository<Loan, Long> {
    Page<Loan> findByUser(User user, Pageable pageable);
    List<Loan> findByBookAndStatus(Book book, LoanEnum status);
    Page<Loan> findByActualReturnDateIsNullAndExpectedReturnDateBefore(LocalDateTime date, Pageable pageable);
}
