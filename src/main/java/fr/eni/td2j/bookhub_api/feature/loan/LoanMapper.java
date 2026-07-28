package fr.eni.td2j.bookhub_api.feature.loan;

import fr.eni.td2j.bookhub_api.common.EntityMapper;
import fr.eni.td2j.bookhub_api.feature.loan.dto.LoanResponseDTO;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
public class LoanMapper implements EntityMapper<Loan, LoanResponseDTO> {
    @Override
    public LoanResponseDTO toDto(Loan loan) {
        return LoanResponseDTO.builder()
                .id(loan.getId())
                .bookId(loan.getBook().getId())
                .userId(loan.getUser().getId())
                .actualReturnDate(
                        Optional.ofNullable(loan.getActualReturnDate())
                                .map(LocalDateTime::toString)
                                .orElse(null)
                )
                .loanDate(loan.getLoanDate().toString())
                .expectedReturnDate(loan.getExpectedReturnDate().toString())
                .status(loan.getStatus().toString())
                .delay(loan.getDelay())
                .build();
    }
}
