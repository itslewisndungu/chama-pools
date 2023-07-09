package chamapool.application.loans.controllers;

import chamapool.application.loans.requests.LoanApplicationRequest;
import chamapool.application.loans.requests.LoanApprovalRequest;
import chamapool.application.loans.services.LoanApplicationsService;
import chamapool.domain.loans.VO.LoanApplicationVO;
import chamapool.domain.loans.VO.LoanApprovalVO;
import chamapool.domain.member.models.Member;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/loans/applications")
@RequiredArgsConstructor
public class LoanApplicationsController {
  private final LoanApplicationsService applicationsService;

  @GetMapping()
  public List<LoanApplicationVO> retrieveLoanApplications() {
    return this.applicationsService.retrieveAllApplications();
  }

  @GetMapping("/active")
  public LoanApplicationVO retrieveMemberActiveLoanApplication(Member member) {
    return this.applicationsService.retrieveMemberActiveApplication(member);
  }

  @GetMapping("/{applicationId}")
  public LoanApplicationVO retrieveLoanApplication(@PathVariable Integer applicationId) {
    return this.applicationsService.retrieveApplication(applicationId);
  }

  @PostMapping("/apply")
  @ResponseStatus(HttpStatus.CREATED)
  public LoanApplicationVO applyForLoan(Member member, @RequestBody LoanApplicationRequest req) {
    return this.applicationsService.applyForLoan(member, req);
  }

  @PostMapping("/{applicationId}/approve")
  @ResponseStatus(HttpStatus.CREATED)
  @PreAuthorize("hasAnyRole('CHAIRMAN', 'TREASURER', 'SECRETARY')")
  public LoanApprovalVO approveLoanApplication(
      Member member, @PathVariable Integer applicationId, @RequestBody LoanApprovalRequest req) {
    return this.applicationsService.approveApplication(member, applicationId, req);
  }
}
