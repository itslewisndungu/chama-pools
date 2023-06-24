package chamapool.application.loans;

import chamapool.application.loans.responses.LoanEligibilityResponse;
import chamapool.domain.loans.VO.LoanVO;
import chamapool.domain.member.models.Member;
import lombok.RequiredArgsConstructor;
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

  @GetMapping("/eligibility")
  public LoanEligibilityResponse checkLoanEligibility(Member member) {
    return this.loansService.checkLoanEligibility(member);
  }
}
