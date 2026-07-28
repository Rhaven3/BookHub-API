package fr.eni.td2j.bookhub_api.feature.loan;


import fr.eni.td2j.bookhub_api.config.BusinessRule;
import fr.eni.td2j.bookhub_api.exception.NotFoundException;
import fr.eni.td2j.bookhub_api.exception.NotOwnedException;
import fr.eni.td2j.bookhub_api.exception.businessRule.BookNotAvailableException;
import fr.eni.td2j.bookhub_api.exception.businessRule.HasDelayException;
import fr.eni.td2j.bookhub_api.exception.businessRule.MaxLoanException;
import fr.eni.td2j.bookhub_api.exception.NotOwnedException;
import fr.eni.td2j.bookhub_api.feature.book.Book;
import fr.eni.td2j.bookhub_api.feature.book.BookRepository;
import fr.eni.td2j.bookhub_api.feature.book.BookService;
import fr.eni.td2j.bookhub_api.feature.loan.dto.LoanDTO;
import fr.eni.td2j.bookhub_api.feature.loan.dto.LoanResponseDTO;
import fr.eni.td2j.bookhub_api.feature.user.Role;
import fr.eni.td2j.bookhub_api.feature.user.User;
import fr.eni.td2j.bookhub_api.feature.user.UserService;
import fr.eni.td2j.bookhub_api.feature.user.dto.response.UserResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class LoanService {
    private final LoanRepository loanRepository;
    private final UserService userService;
    private final BookService bookService;
    private final BookRepository bookRepository;
    private final LoanMapper loanMapper;

    public LoanService(LoanRepository loanRepository, BookRepository bookRepository, LoanMapper loanMapper, UserService userService, BookService bookService) {
        this.loanRepository = loanRepository;
        this.bookRepository = bookRepository;
        this.userService = userService;
        this.bookService = bookService;
        this.loanMapper = loanMapper;
    }


    public Loan create(LoanDTO loanDTO, UserDetails userDetails) {
        if (loanDTO == null) {
            throw new IllegalArgumentException("LoanDTO cannot be null");
        }
        // check if user is connected
        UserResponseDTO userResponseDTO = userService.getCurrentUser(userDetails.getUsername());
        User user = userService.findByEmail(userResponseDTO.getEmail());
        if (user == null) {
            throw new NotFoundException("User not connected");
        }
        Book book = bookRepository.findById(loanDTO.getBookId())
                .orElseThrow(() -> new NotFoundException("Livre introuvable."));

        if (!book.isAvailable()) {
            throw new BookNotAvailableException("Book is not available");
        }
        // check if user has reached maximum loan
        List<Loan> loans = loanRepository.findByUser(user, Pageable.ofSize(1000)).getContent();
        if (loans.size() == BusinessRule.RGLOAN01) {
            throw new MaxLoanException("le maximum d'emprunt est atteint (3)");
        }
        // chech if user has delay
        if (hasDelay(loans)) {
            throw new HasDelayException("l'utilisateur à un retour en retard !!");
        }
        // Book is now unavailable
        book.setAvailable(false);
        bookService.update(loanDTO.bookId, book);
        // create Loan
        LocalDateTime now = LocalDateTime.now();
        Loan loan = Loan.builder()
                .loanDate(now)
                .expectedReturnDate(now.plusDays(BusinessRule.RGLOAN02))
                .status(LoanEnum.IN_PROGRESS)
                .user(user)
                .book(book)
                .build();
        return loanRepository.save(loan);
    }

    private boolean hasDelay(List<Loan> loansUser) {
        int delay = 0;
        for (Loan loan : loansUser) {
            delay += loan.getDelay();
        }
        if (delay > -1) {
            return true;
        }
        return false;
    }

    public Page<LoanResponseDTO> findByConnectedUser(UserDetails userDetails, Pageable pageable) {
        UserResponseDTO userResponseDTO = userService.getCurrentUser(userDetails.getUsername());
        User user = userService.findByEmail(userResponseDTO.getEmail());
        if (user == null) {
            throw new NotFoundException("User not connected");
        }
        return loanRepository.findByUser(user, pageable).map(loanMapper::toDto);
    }

    public Page<LoanResponseDTO> findAll(Pageable pageable) {
        Page<Loan> loansPage = loanRepository.findAll(pageable);
        return loansPage.map(loanMapper::toDto);
    }

    public LoanResponseDTO returnLoan(Long id, UserDetails userDetails) {
        // check if user is connected
        UserResponseDTO userResponseDTO = userService.getCurrentUser(userDetails.getUsername());
        User user = userService.findByEmail(userResponseDTO.getEmail());
        if (user == null) {
            throw new NotFoundException("User not connected");
        }
        // check if loan exists
        Loan loan = loanRepository.findById(id).orElse(null);
        if (loan == null) {
            throw new NotFoundException("Loan not found");
        }
        // check if loan belongs to user
        if (!loan.getUser().equals(user) && user.getRole().equals(Role.USER.name())) {
            throw new NotOwnedException("l'utilisateur ne possède pas ce livre");
        }
        // check if book is available and update book status
        Book book = loan.getBook();
        boolean bookAvailable = bookService.isBookAvailable(book);
        if (bookAvailable) {
            book.setAvailable(true);
            bookService.update(book.getId(), book);
        }

        loan.setActualReturnDate(LocalDateTime.now());
        loan.setStatus(LoanEnum.RETURNED);
        return loanMapper.toDto(loanRepository.save(loan));
    }
}
