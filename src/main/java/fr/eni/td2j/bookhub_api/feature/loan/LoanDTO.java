package fr.eni.td2j.bookhub_api.feature.loan;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
@Data
public class LoanDTO {
    public LocalDateTime expectedReturnDate;
    public Long bookId;
}