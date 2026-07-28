package fr.eni.td2j.bookhub_api.feature.loan;


import fr.eni.td2j.bookhub_api.config.BusinessRule;
import fr.eni.td2j.bookhub_api.exception.NotFoundException;
import fr.eni.td2j.bookhub_api.exception.NotOwnedException;
import fr.eni.td2j.bookhub_api.exception.businessRule.BookNotAvailableException;
import fr.eni.td2j.bookhub_api.exception.businessRule.HasDelayException;
import fr.eni.td2j.bookhub_api.exception.businessRule.MaxLoanException;
import fr.eni.td2j.bookhub_api.feature.book.Book;
import fr.eni.td2j.bookhub_api.feature.book.BookRepository;
import fr.eni.td2j.bookhub_api.feature.loan.dto.LoanDTO;
import fr.eni.td2j.bookhub_api.feature.loan.dto.LoanResponseDTO;
import fr.eni.td2j.bookhub_api.feature.user.Role;
import fr.eni.td2j.bookhub_api.feature.user.User;
import fr.eni.td2j.bookhub_api.feature.user.UserRepository;
import fr.eni.td2j.bookhub_api.feature.user.UserService;
import fr.eni.td2j.bookhub_api.feature.user.dto.response.UserResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LoanService {
    private final LoanRepository loanRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final BookRepository bookRepository;
    private final LoanMapper loanMapper;


    @Transactional
    public Loan create(LoanDTO loanDTO, UserDetails userDetails) {

        if (loanDTO == null) {
            throw new IllegalArgumentException("LoanDTO cannot be null");
        }

        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new NotFoundException("Utilisateur introuvable."));

        Book book = bookRepository.findById(loanDTO.getBookId())
                .orElseThrow(() -> new NotFoundException("Livre introuvable."));

        if (!book.isAvailable()) {
            throw new BookNotAvailableException("Le livre n'est pas disponible.");
        }

        List<Loan> loans = loanRepository
                .findByUser(user, Pageable.ofSize(1000))
                .getContent();

        if (loans.size() >= BusinessRule.RGLOAN01) {
            throw new MaxLoanException("Le maximum d'emprunts est atteint (3).");
        }

        if (hasDelay(loans)) {
            throw new HasDelayException("L'utilisateur a un retour en retard.");
        }

        book.setAvailable(false);
        bookRepository.save(book);

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
        for (Loan loan : loansUser) {
            if (loan.getDelay() > 0) {
                return true;
            }
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
            throw new NotOwnedException("l'utilisateur ne possède pas cette emprunt");
        }
        // check if book is available and update book status
        Book book = loan.getBook();

        if (!book.isAvailable()) {
            book.setAvailable(true);
            bookRepository.save(book);
        }

        loan.setActualReturnDate(LocalDateTime.now());
        loan.setStatus(LoanEnum.RETURNED);
        return loanMapper.toDto(loanRepository.save(loan));
    }
}
