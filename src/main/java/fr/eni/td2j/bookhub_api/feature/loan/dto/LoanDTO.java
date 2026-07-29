package fr.eni.td2j.bookhub_api.feature.loan.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
@Data
public class LoanDTO {
    public Long bookId;
}