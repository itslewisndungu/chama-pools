package chamapool.application.loans;

import chamapool.application.loans.requests.LoanApplicationRequest;
import chamapool.domain.loans.Loan;
import chamapool.domain.loans.LoanApplication;
import chamapool.domain.loans.VO.LoanApplicationVO;
import chamapool.domain.loans.enums.LoanApprovalStatus;
import chamapool.domain.loans.repositories.LoanApplicationRepository;
import chamapool.domain.loans.repositories.LoanApprovalRepository;
import chamapool.domain.loans.repositories.LoanRepository;
import chamapool.domain.member.models.Member;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoansService {
  private final LoanRepository loanRepository;
  private final LoanApprovalRepository loanApprovalRepository;
  private final LoanApplicationRepository loanApplicationRepository;

  @Transactional
  public LoanApplicationVO applyForLoan(Member member, LoanApplicationRequest req) {
    log.info("Member {} applying for loan: {}", member.firstName() + ' ' + member.lastName(), req);

    Loan loan = new Loan().member(member).amount(req.amount()).reasonForLoan(req.reasonForLoan());

    loanRepository.save(loan);

    LoanApplication application =
        new LoanApplication().loan(loan).approvalStatus(LoanApprovalStatus.AWAITING_APPROVAL);

    loanApplicationRepository.save(application);

    return new LoanApplicationVO(application);
  }

  public void approveLoan(Member member, Integer loanId) {
    //    log.info("Member {} approving loan: {}", member.firstName() + ' ' + member.lastName(),
    // loanId);
    //
    //    Loan loan = loanRepository.getReferenceById(loanId);
    //
    //    var roles = member.getRoles();
    //    if (roles.contains(MemberRole.CHAIRMAN)) {
    //      loan.approval().chairman(member);
    //    } else if (roles.contains(MemberRole.SECRETARY)) {
    //      loan.approval().secretary(member);
    //    } else if (roles.contains(MemberRole.TREASURER)) {
    //      loan.approval().treasurer(member);
    //    } else {
    //      throw new RuntimeException("You are not authorized to approve loans");
    //    }
    //
    //    loanRepository.save(loan);
  }
}
