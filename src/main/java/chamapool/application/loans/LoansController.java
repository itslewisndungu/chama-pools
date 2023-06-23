package chamapool.application.loans;

import chamapool.domain.loans.VO.LoanVO;
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

}
