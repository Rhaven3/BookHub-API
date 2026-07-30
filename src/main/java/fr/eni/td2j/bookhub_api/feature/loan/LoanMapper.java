package fr.eni.td2j.bookhub_api.feature.loan;

import fr.eni.td2j.bookhub_api.common.EntityMapper;
import fr.eni.td2j.bookhub_api.feature.book.dto.mapper.BookMapper;
import fr.eni.td2j.bookhub_api.feature.loan.dto.LoanResponseDTO;
import fr.eni.td2j.bookhub_api.feature.user.UserMapper;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
public class LoanMapper implements EntityMapper<Loan, LoanResponseDTO> {
    private final BookMapper bookMapper;
    private final UserMapper userMapper;

    public LoanMapper(BookMapper bookMapper, UserMapper userMapper) {
        this.bookMapper = bookMapper;
        this.userMapper = userMapper;
    }

    @Override
    public LoanResponseDTO toDto(Loan loan) {
        return LoanResponseDTO.builder()
                .id(loan.getId())
                .book(bookMapper.toDto(loan.getBook()))
                .user(userMapper.toDto(loan.getUser()))
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
