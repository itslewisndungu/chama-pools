package chamapool.application.loans;

import chamapool.application.loans.requests.LoanApplicationRequest;
import chamapool.application.loans.requests.LoanApprovalRequest;
import chamapool.domain.loans.VO.LoanApplicationVO;
import chamapool.domain.loans.VO.LoanApprovalVO;
import chamapool.domain.member.models.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/loans/applications")
@RequiredArgsConstructor
public class LoanApplicationsController {
  private final LoansService loansService;

  @GetMapping("/active-application")
  public LoanApplicationVO retrieveActiveLoanApplication(Member member) {
    return this.loansService.getMemberActiveLoanApplication(member);
  }

  @GetMapping("/{applicationId}")
  public LoanApplicationVO retrieveLoanApplication(@PathVariable Integer applicationId) {
    return this.loansService.retrieveLoanApplication(applicationId);
  }

  @PostMapping("/apply")
  @ResponseStatus(HttpStatus.CREATED)
  public LoanApplicationVO applyForLoan(Member member, @RequestBody LoanApplicationRequest req) {
    return this.loansService.applyForLoan(member, req);
  }

  @PostMapping("/{applicationId}/approve")
  @ResponseStatus(HttpStatus.CREATED)
  @PreAuthorize("hasAnyRole('CHAIRMAN', 'TREASURER', 'SECRETARY')")
  public LoanApprovalVO approveLoan(
      Member member, @PathVariable Integer applicationId, @RequestBody LoanApprovalRequest req) {
    return this.loansService.approveLoan(member, applicationId, req);
  }
}
