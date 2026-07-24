package fr.eni.td2j.bookhub_api.service;

import fr.eni.td2j.bookhub_api.feature.loan.Loan;
import fr.eni.td2j.bookhub_api.feature.loan.LoanService;
import fr.eni.td2j.bookhub_api.feature.user.User;
import fr.eni.td2j.bookhub_api.feature.user.UserRepository;
import fr.eni.td2j.bookhub_api.feature.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class LoanServiceTest {
    private final LoanService loanService;
    private final UserService userService;

    private Loan loan;
    private User user;

    public LoanServiceTest(LoanService loanService, UserService userService) {
        this.loanService = loanService;
        this.userService = userService;
    }

    @BeforeEach
    void setUp() {
        user = User.builder()
                .
                .build();

        loan = Loan.builder()
                .
                .build();
    }

    @Test
    void create() {

    }
}
