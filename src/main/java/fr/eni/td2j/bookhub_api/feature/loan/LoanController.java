package fr.eni.td2j.bookhub_api.feature.loan;

import fr.eni.td2j.bookhub_api.feature.loan.dto.LoanDTO;
import fr.eni.td2j.bookhub_api.feature.loan.dto.LoanResponseDTO;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/loans")
public class LoanController {
    private final LoanService loanService;
    private final LoanMapper loanMapper;

    public LoanController(LoanService loanService, LoanMapper loanMapper) {
        this.loanService = loanService;
        this.loanMapper = loanMapper;
    }

    @GetMapping
    public ResponseEntity<Page<LoanResponseDTO>> findAll(Pageable pageable) {
        return ResponseEntity.ok(loanService.findAll(pageable));
    }
    @GetMapping("/all-delay")
    public ResponseEntity<Page<LoanResponseDTO>> findAllDelayed(Pageable pageable) {
        return ResponseEntity.ok(loanService.findAllDelayed(pageable));
    }

    @GetMapping("/me")
    public ResponseEntity<Page<LoanResponseDTO>> findAllByUser(@AuthenticationPrincipal UserDetails userDetails, Pageable pageable) {
        return ResponseEntity.ok(loanService.findByConnectedUser(userDetails, pageable));
    }


    @PostMapping
    public ResponseEntity<LoanResponseDTO> create(@Valid @RequestBody LoanDTO loanDTO, @AuthenticationPrincipal UserDetails userDetails) {
        Loan created = loanService.create(loanDTO, userDetails);
        return ResponseEntity.status(HttpStatus.CREATED).body(loanMapper.toDto(created));
    }

    @GetMapping("{id}/return")
    public ResponseEntity<LoanResponseDTO> returnLoan( @PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(loanService.returnLoan(id, userDetails));
    }
}

