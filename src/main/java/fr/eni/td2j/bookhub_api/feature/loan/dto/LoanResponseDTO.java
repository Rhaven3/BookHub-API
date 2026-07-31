package fr.eni.td2j.bookhub_api.feature.loan.dto;

import fr.eni.td2j.bookhub_api.feature.book.dto.BookResponseDTO;
import fr.eni.td2j.bookhub_api.feature.user.dto.response.UserResponseDTO;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoanResponseDTO {
    private Long id;
    private BookResponseDTO book;
    private UserResponseDTO user;
    private String loanDate;
    private String expectedReturnDate;
    private String actualReturnDate;
    private String status;
    private int delay;
}
