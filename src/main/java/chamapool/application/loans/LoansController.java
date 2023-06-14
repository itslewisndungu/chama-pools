package chamapool.application.loans;

import chamapool.application.loans.requests.LoanApplicationRequest;
import chamapool.application.loans.requests.LoanApprovalRequest;
import chamapool.domain.loans.VO.LoanApplicationVO;
import chamapool.domain.loans.VO.LoanApprovalVO;
import chamapool.domain.loans.VO.LoanVO;
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

  @GetMapping("/{loanId}")
  public LoanVO retrieveLoan(@PathVariable Integer loanId) {
    return this.loansService.retrieveLoan(loanId);
  }

  @GetMapping("/{loanId}/application")
    public LoanApplicationVO retrieveLoanApplication(@PathVariable Integer loanId) {
        return this.loansService.retrieveLoanApplication(loanId);
    }

  @PostMapping("/apply")
  @ResponseStatus(HttpStatus.CREATED)
  public LoanApplicationVO applyForLoan(Member member, @RequestBody LoanApplicationRequest req) {
    return this.loansService.applyForLoan(member, req);
  }

  @PostMapping("/{loanId}/approve")
  @ResponseStatus(HttpStatus.CREATED)
  @PreAuthorize("hasAnyRole('CHAIRMAN', 'TREASURER', 'SECRETARY')")
  public LoanApprovalVO approveLoan(
      Member member, @PathVariable Integer loanId, @RequestBody LoanApprovalRequest req) {
    return this.loansService.approveLoan(member, loanId, req);
  }
}
