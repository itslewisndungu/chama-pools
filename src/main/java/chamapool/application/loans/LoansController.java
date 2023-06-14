package chamapool.application.loans;

import chamapool.application.loans.requests.LoanApplicationRequest;
import chamapool.domain.loans.VO.LoanApplicationVO;
import chamapool.domain.member.models.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/loans")
@RequiredArgsConstructor
public class LoansController {
  private final LoansService loansService;

  @PostMapping("/apply")
  @ResponseStatus(HttpStatus.CREATED)
  public LoanApplicationVO applyForLoan(Member member, @RequestBody LoanApplicationRequest req) {
    return this.loansService.applyForLoan(member, req);
  }

  @PostMapping("/approve/{loanId}")
  @PreAuthorize("hasAnyRole('CHAIRMAN', 'TREASURER', 'SECRETARY')")
  public void approveLoan(Member member, @PathVariable Integer loanId) {
    this.loansService.approveLoan(member, loanId);
  }
}
