package chamapool.application.loans;

import chamapool.application.loans.requests.LoanApplicationRequest;
import chamapool.application.loans.requests.LoanApprovalRequest;
import chamapool.domain.loans.Loan;
import chamapool.domain.loans.LoanApplication;
import chamapool.domain.loans.LoanApproval;
import chamapool.domain.loans.LoanApprovalId;
import chamapool.domain.loans.VO.LoanApplicationVO;
import chamapool.domain.loans.VO.LoanApprovalVO;
import chamapool.domain.loans.VO.LoanVO;
import chamapool.domain.loans.enums.LoanApprovalStatus;
import chamapool.domain.loans.repositories.LoanApplicationRepository;
import chamapool.domain.loans.repositories.LoanApprovalRepository;
import chamapool.domain.loans.repositories.LoanRepository;
import chamapool.domain.member.models.Member;
import chamapool.domain.member.repositories.MemberRepository;
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

  @Transactional
  public LoanApprovalVO approveLoan(Member member, Integer loanId, LoanApprovalRequest req) {
    var loanApplication = loanRepository.getReferenceById(loanId).loanApplication();

    log.info(
        "{} approving {}", member.firstName() + ' ' + member.lastName(), loanApplication.loan());

    var approval =
        new LoanApproval()
            .id(new LoanApprovalId())
            .stakeholder(member)
            .loanApplication(loanApplication)
            .message(req.message());

    if (req.approved()) {
      approval.status(LoanApprovalStatus.APPROVED);
    } else {
      approval.status(LoanApprovalStatus.REJECTED);
    }

    loanApprovalRepository.save(approval);

    return new LoanApprovalVO(approval);
  }

  public LoanVO retrieveLoan(Integer loanId) {
    var loan = this.loanRepository.getReferenceById(loanId);
    return new LoanVO(loan);
  }

  public LoanApplicationVO retrieveLoanApplication(Integer loanId) {
    var loanApplication = this.loanRepository.getReferenceById(loanId).loanApplication();
    return new LoanApplicationVO(loanApplication);
  }
}
