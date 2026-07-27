package fr.eni.td2j.bookhub_api.feature.loan;

import fr.eni.td2j.bookhub_api.exception.BookNotAvailableException;
import fr.eni.td2j.bookhub_api.exception.NotFoundException;
import fr.eni.td2j.bookhub_api.exception.UserNotConnectedException;
import fr.eni.td2j.bookhub_api.feature.book.Book;
import fr.eni.td2j.bookhub_api.feature.book.BookService;
import fr.eni.td2j.bookhub_api.feature.user.User;
import fr.eni.td2j.bookhub_api.feature.user.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

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
        User user = userService.getConnectedUser(userDetails);
        if (user == null) {
            throw new UserNotConnectedException("User not connected");
        }
        Book book = bookService.findById(loanDTO.bookId).orElse(null);
        if (book == null) {
            throw new NotFoundException("Book not found");
        }
        if (!book.isAvailable()) {
            throw new BookNotAvailableException("Book is not available");
        }

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

    public Object update(Long id, Loan loan) {
        return null;
    }
}
