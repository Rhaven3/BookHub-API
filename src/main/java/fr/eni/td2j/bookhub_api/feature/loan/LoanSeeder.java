package fr.eni.td2j.bookhub_api.feature.loan;

import fr.eni.td2j.bookhub_api.feature.book.Book;
import fr.eni.td2j.bookhub_api.feature.book.BookRepository;
import fr.eni.td2j.bookhub_api.feature.user.User;
import fr.eni.td2j.bookhub_api.feature.user.UserRepository;
import fr.eni.td2j.bookhub_api.seeder.EntitySeeder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class LoanSeeder implements EntitySeeder {

    private final LoanRepository loanRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    public LoanSeeder(LoanRepository loanRepository,
                      UserRepository userRepository,
                      BookRepository bookRepository) {
        this.loanRepository = loanRepository;
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
    }

    @Override
    public void seed() {
        if (loanRepository.count() > 0) return;

        User alice = userRepository.findAll().stream()
                .filter(u -> u.getEmail().equals("alice.bernard@mail.com"))
                .findFirst()
                .orElseThrow();

        User bob = userRepository.findAll().stream()
                .filter(u -> u.getEmail().equals("bob.petit@mail.com"))
                .findFirst()
                .orElseThrow();

        Book book1984 = bookRepository.findAll().stream()
                .filter(b -> b.getTitle().equals("1984"))
                .findFirst()
                .orElseThrow();

        Book bookDune = bookRepository.findAll().stream()
                .filter(b -> b.getTitle().equals("Dune"))
                .findFirst()
                .orElseThrow();

        // Emprunt en cours, dans les temps
        Loan loanInProgress = Loan.builder()
                .loanDate(LocalDateTime.now().minusDays(5))
                .expectedReturnDate(LocalDateTime.now().plusDays(9))
                .actualReturnDate(null)
                .status(LoanEnum.IN_PROGRESS)
                .user(alice)
                .book(book1984)
                .build();

        // Emprunt en retard
        Loan loanLate = Loan.builder()
                .loanDate(LocalDateTime.now().minusDays(20))
                .expectedReturnDate(LocalDateTime.now().minusDays(6))
                .actualReturnDate(null)
                .status(LoanEnum.IN_PROGRESS)
                .user(bob)
                .book(bookDune)
                .build();

        // Emprunt déjà rendu
        Loan loanReturned = Loan.builder()
                .loanDate(LocalDateTime.now().minusDays(30))
                .expectedReturnDate(LocalDateTime.now().minusDays(16))
                .actualReturnDate(LocalDateTime.now().minusDays(18))
                .status(LoanEnum.RETURNED)
                .user(alice)
                .book(bookDune)
                .build();

        loanRepository.save(loanInProgress);
        loanRepository.save(loanLate);
        loanRepository.save(loanReturned);
    }

    @Override
    public int order() {
        return 3; // après UserSeeder et BookSeeder
    }
}
