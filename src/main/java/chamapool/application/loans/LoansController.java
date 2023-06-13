package chamapool.application.loans;

import chamapool.application.loans.requests.LoanApplicationRequest;
import chamapool.domain.member.models.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/loans")
@RequiredArgsConstructor
public class LoansController {
  private final LoansService loansService;

  @PostMapping("/apply")
  @ResponseStatus(HttpStatus.CREATED)
  public void applyForLoan(Member member, @RequestBody LoanApplicationRequest req) {
    this.loansService.applyForLoan(member, req);
  }
}
