package fr.eni.td2j.bookhub_api.feature.loan;

import fr.eni.td2j.bookhub_api.exception.BookNotAvailableException;
import fr.eni.td2j.bookhub_api.exception.NotFoundException;
import fr.eni.td2j.bookhub_api.feature.book.Book;
import fr.eni.td2j.bookhub_api.feature.book.BookService;
import fr.eni.td2j.bookhub_api.feature.loan.dto.LoanDTO;
import fr.eni.td2j.bookhub_api.feature.loan.dto.LoanResponseDTO;
import fr.eni.td2j.bookhub_api.feature.user.User;
import fr.eni.td2j.bookhub_api.feature.user.UserService;
import fr.eni.td2j.bookhub_api.feature.user.dto.response.UserResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class LoanService {
    private final LoanRepository loanRepository;
    private final UserService userService;
    private final BookService bookService;

    public LoanService(LoanRepository loanRepository, UserService userService, BookService bookService) {
        this.loanRepository = loanRepository;
        this.userService = userService;
        this.bookService = bookService;
    }


    public Loan create(LoanDTO loanDTO, UserDetails userDetails) {
        if (loanDTO == null) {
            throw new IllegalArgumentException("LoanDTO cannot be null");
        }
        UserResponseDTO userResponseDTO = userService.getCurrentUser(userDetails.getUsername());
        User user = userService.findByEmail(userResponseDTO.getEmail());
        if (user == null) {
            throw new NotFoundException("User not connected");
        }

        // check if the book have reservations in progress

        Book book = bookService.findById(loanDTO.bookId).orElse(null);
        if (book == null) {
            throw new NotFoundException("Book not found");
        }
        if (!book.isAvailable()) {
            throw new BookNotAvailableException("Book is not available");
        }
        // Book is now unavailable
        book.setAvailable(false);
        bookService.update(loanDTO.bookId, book);

        // create Loan
        LocalDateTime now = LocalDateTime.now();
        Loan loan = Loan.builder()
                .loanDate(now)
                .expectedReturnDate(loanDTO.expectedReturnDate)
                .status(LoanEnum.IN_PROGRESS)
                .user(user)
                .book(book)
                .build();
        return loanRepository.save(loan);
    }

    public Page<LoanResponseDTO> findByConnectedUser(UserDetails userDetails, Pageable pageable) {
        UserResponseDTO userResponseDTO = userService.getCurrentUser(userDetails.getUsername());
        User user = userService.findByEmail(userResponseDTO.getEmail());
        if (user == null) {
            throw new NotFoundException("User not connected");
        }
        return loanRepository.findByUser(user, pageable).map(loan -> LoanResponseDTO.builder()
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
                .build());
    }

    public Page<LoanResponseDTO> findAll(Pageable pageable) {
        Page<Loan> loansPage = loanRepository.findAll(pageable);
        return loansPage.map(loan -> LoanResponseDTO.builder()
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
                .build()
        );
    }

    public Loan returnLoan(Long id, UserDetails userDetails) {
        UserResponseDTO userResponseDTO = userService.getCurrentUser(userDetails.getUsername());
        User user = userService.findByEmail(userResponseDTO.getEmail());
        if (user == null) {
            throw new NotFoundException("User not connected");
        }
        Loan loan = loanRepository.findById(id).orElse(null);
        if (loan == null) {
            throw new NotFoundException("Loan not found");
        }
        loan.setActualReturnDate(LocalDateTime.now());
        loan.setStatus(LoanEnum.RETURNED);
        return loanRepository.save(loan);
    }
}
