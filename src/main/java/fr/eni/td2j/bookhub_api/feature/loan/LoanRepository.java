package fr.eni.td2j.bookhub_api.feature.loan;

import aj.org.objectweb.asm.commons.Remapper;
import fr.eni.td2j.bookhub_api.feature.book.Book;
import fr.eni.td2j.bookhub_api.feature.loan.dto.LoanResponseDTO;
import fr.eni.td2j.bookhub_api.feature.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface LoanRepository extends JpaRepository<Loan, Long> {
    Page<Loan> findByUser(User user, Pageable pageable);
    List<Loan> findByBookAndStatus(Book book, LoanEnum status);

    @Query("""
        SELECT l
        FROM Loan l
        WHERE l.user = :user
        AND (:status IS NULL OR l.status = :status)
        AND (:date IS NULL OR FUNCTION('DATE', l.loanDate) = :date)
        """)
    Page<Loan> findByUserAndFilters(
            @Param("user") User user,
            @Param("status") LoanEnum status,
            @Param("date") LocalDate date,
            Pageable pageable
    );
}
