package fr.eni.td2j.bookhub_api.feature.loan;

import fr.eni.td2j.bookhub_api.feature.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoanRepository extends JpaRepository<Loan, Long> {
    Page<Loan> findByUser(User user, Pageable pageable);
}
