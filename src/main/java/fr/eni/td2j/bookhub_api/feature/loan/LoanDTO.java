package fr.eni.td2j.bookhub_api.feature.loan;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public class LoanDTO {
    public LocalDateTime expectedReturnDate;
    public Long bookId;
}