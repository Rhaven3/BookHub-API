package fr.eni.td2j.bookhub_api.service;

import fr.eni.td2j.bookhub_api.exception.BookNotAvailableException;
import fr.eni.td2j.bookhub_api.exception.NotFoundException;
import fr.eni.td2j.bookhub_api.exception.UserNotConnectedException;
import fr.eni.td2j.bookhub_api.feature.author.Author;
import fr.eni.td2j.bookhub_api.feature.book.Book;
import fr.eni.td2j.bookhub_api.feature.book.BookService;
import fr.eni.td2j.bookhub_api.feature.category.Category;
import fr.eni.td2j.bookhub_api.feature.editor.Editor;
import fr.eni.td2j.bookhub_api.feature.loan.Loan;
import fr.eni.td2j.bookhub_api.feature.loan.LoanDTO;
import fr.eni.td2j.bookhub_api.feature.loan.LoanRepository;
import fr.eni.td2j.bookhub_api.feature.loan.LoanService;
import fr.eni.td2j.bookhub_api.feature.user.User;
import fr.eni.td2j.bookhub_api.feature.user.UserService;
import fr.eni.td2j.bookhub_api.security.CustomUserDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LoanServiceTest {
    @InjectMocks
    private LoanService loanService;
    @Mock
    private LoanRepository loanRepository;
    @Mock
    private BookService bookService;
    @Mock
    private UserService userService;

    private LoanDTO loanDTO;
    private User user;
    private UserDetails userDetails;
    private Book book;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .firstName("Jean")
                .lastName("Dupont")
                .email("test@test.fr")
                .phone("0600000000")
                .password("password")
                .role("USER")
                .build();
        userDetails = new CustomUserDetails(user);

        book = Book.builder()
                .title("Le Petit Prince")
                .description("Un conte poétique et philosophique.")
                .publishDate(LocalDate.of(1943, 3, 6)) // 6 avril 1943
                .language("Français")
                .isbn("123456789")
                .available(true)
                .owned(true)
                .categories(List.of(Category.builder()
                        .name("Littérature")
                        .build()
                ))
                .authors(List.of( Author.builder()
                        .fname("Antoine")
                        .lname("de Saint-Exupéry")
                        .build()
                ))
                .editor(Editor.builder()
                        .name("Gallimard")
                        .build()
                )
                .build();

        LocalDateTime date = LocalDateTime.now().plusWeeks(2);
        loanDTO = LoanDTO.builder()
                .bookId(1L)
                .expectedReturnDate(date)
                .build();
    }

    @Test
    void create_shouldSaveLoanWithCorrectData_whenBookIsAvailable() {
        when(userService.getConnectedUser(any(UserDetails.class))).thenReturn(user);
        when(bookService.findById(anyLong())).thenReturn(Optional.of(book));
        when(loanRepository.save(any(Loan.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Loan loan = loanService.create(loanDTO, userDetails);
        verify(loanRepository).save(any(Loan.class));

        ArgumentCaptor<Loan> loanCaptor = ArgumentCaptor.forClass(Loan.class);
        verify(loanRepository).save(loanCaptor.capture());

        Loan savedLoan = loanCaptor.getValue();
        assertEquals(user, savedLoan.getUser());
        assertEquals(book, savedLoan.getBook());
        assertEquals(loanDTO.getExpectedReturnDate(), savedLoan.getExpectedReturnDate());
        assertNotNull(loan);
    }

    @Test
    void create_shouldThrowException_whenBookIsNotAvailable() {
        book.setAvailable(false);
        when(userService.getConnectedUser(any(UserDetails.class))).thenReturn(user);
        when(bookService.findById(anyLong())).thenReturn(Optional.of(book));

        assertThrows(BookNotAvailableException.class,
                () -> loanService.create(loanDTO, userDetails));

        verify(loanRepository, never()).save(any(Loan.class));
    }

    @Test
    void create_shouldThrowException_whenBookNotFound() {
        when(userService.getConnectedUser(any(UserDetails.class))).thenReturn(user);
        when(bookService.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> loanService.create(loanDTO, userDetails));

        verify(loanRepository, never()).save(any(Loan.class));
    }

    @Test
    void create_shouldThrowException_whenUserNotConnected() {
        when(userService.getConnectedUser(any(UserDetails.class))).thenReturn(null);
        assertThrows(UserNotConnectedException.class,
                () -> loanService.create(loanDTO, userDetails));
    }

    @Test
    void create_shouldThrowException_whenLoanDTOIsNull() {
        assertThrows(IllegalArgumentException.class, () -> loanService.create(null, userDetails));
    }
}
