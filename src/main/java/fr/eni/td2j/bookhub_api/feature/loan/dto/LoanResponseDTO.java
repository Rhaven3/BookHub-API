package fr.eni.td2j.bookhub_api.feature.loan.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoanResponseDTO {
    private Long id;
    private Long bookId;
    private Long userId;
    private String loanDate;
    private String expectedReturnDate;
    private String actualReturnDate;
    private String status;
    private int delay;
}
