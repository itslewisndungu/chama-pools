package chamapool.application.loans;

import chamapool.application.loans.requests.LoanApplicationRequest;
import chamapool.application.loans.requests.LoanApprovalRequest;
import chamapool.application.loans.responses.LoanEligibilityResponse;
import chamapool.domain.loans.Loan;
import chamapool.domain.loans.LoanApplication;
import chamapool.domain.loans.LoanApproval;
import chamapool.domain.loans.LoanApprovalId;
import chamapool.domain.loans.VO.*;
import chamapool.domain.loans.enums.LoanApprovalStatus;
import chamapool.domain.loans.enums.LoanStatus;
import chamapool.domain.loans.repositories.LoanApplicationRepository;
import chamapool.domain.loans.repositories.LoanApprovalRepository;
import chamapool.domain.loans.repositories.LoanRepository;
import chamapool.domain.member.enums.MemberRole;
import chamapool.domain.member.models.Member;
import chamapool.domain.member.repositories.MemberRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
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
  private final MemberRepository memberRepository;

  @Transactional
  public LoanApplicationVO applyForLoan(Member applicant, LoanApplicationRequest req) {
    log.info(
        "Member {} applying for loan of amount: {}",
        applicant.firstName() + ' ' + applicant.lastName(),
        req.amount());

    var existingApplication = this.retrieveActiveLoanApplication(applicant);
    if (existingApplication.isPresent()) {
      throw new RuntimeException("You already have an active loan application");
    }

    var application =
        new LoanApplication()
            .member(applicant)
            .amount(req.amount())
            .reasonForLoan(req.reasonForLoan());
    loanApplicationRepository.save(application);

    var approvals = this.retrieveLoanApprovals(application);
    return new LoanApplicationVO(application, approvals);
  }

  @Transactional
  public LoanApprovalVO approveLoan(
      Member stakeholder, Integer applicationId, LoanApprovalRequest req) {
    var loanApplication = loanApplicationRepository.getReferenceById(applicationId);

    log.info(
        "{} approving {}", stakeholder.firstName() + ' ' + stakeholder.lastName(), loanApplication);

    var approval =
        new LoanApproval()
            .id(new LoanApprovalId())
            .stakeholder(stakeholder)
            .loanApplication(loanApplication)
            .message(req.message());

    if (req.approved()) {
      approval.status(LoanApprovalStatus.APPROVED);
    } else {
      approval.status(LoanApprovalStatus.REJECTED);
    }

    loanApprovalRepository.save(approval);

    this.generateLoanOnSuccessfulApproval(loanApplication);

    return new LoanApprovalVO(approval);
  }

  public LoanVO retrieveLoan(Integer loanId) {
    var loan = this.loanRepository.getReferenceById(loanId);
    return new LoanVO(loan);
  }

  public LoanApplicationVO retrieveLoanApplication(Integer applicationId) {
    var loanApplication = this.loanApplicationRepository.getReferenceById(applicationId);
    var approvals = this.retrieveLoanApprovals(loanApplication);
    return new LoanApplicationVO(loanApplication, approvals);
  }

  public LoanApplicationVO getMemberActiveLoanApplication(Member member) {
    var application = this.retrieveActiveLoanApplication(member).orElseThrow();

    return new LoanApplicationVO(application, this.retrieveLoanApprovals(application));
  }

  public LoanEligibilityResponse checkLoanEligibility(Member member) {
    var application = this.retrieveActiveLoanApplication(member);

    if (application.isPresent()) {
      return new LoanEligibilityResponse(false, null, "You have an active loan application");
    }

    return new LoanEligibilityResponse(true, 100000, null);
  }

  private Approvals retrieveLoanApprovals(LoanApplication loanApplication) {
    var chairman =
        this.memberRepository.getMembersByRole(MemberRole.CHAIRMAN).stream()
            .findFirst()
            .orElseThrow();

    var secretary =
        this.memberRepository.getMembersByRole(MemberRole.SECRETARY).stream()
            .findFirst()
            .orElseThrow();

    var treasurer =
        this.memberRepository.getMembersByRole(MemberRole.TREASURER).stream()
            .findFirst()
            .orElseThrow();

    var chairmanApproval =
        loanApprovalRepository
            .getLoanApprovalsByStakeholderAndLoanApplication(chairman, loanApplication)
            .map(approval -> new Approval(approval.status(), approval.message()))
            .orElse(new Approval(LoanApprovalStatus.AWAITING_APPROVAL, null));

    var secretaryApproval =
        loanApprovalRepository
            .getLoanApprovalsByStakeholderAndLoanApplication(secretary, loanApplication)
            .map(approval -> new Approval(approval.status(), approval.message()))
            .orElse(new Approval(LoanApprovalStatus.AWAITING_APPROVAL, null));

    var treasurerApproval =
        loanApprovalRepository
            .getLoanApprovalsByStakeholderAndLoanApplication(treasurer, loanApplication)
            .map(approval -> new Approval(approval.status(), approval.message()))
            .orElse(new Approval(LoanApprovalStatus.AWAITING_APPROVAL, null));

    return new Approvals(chairmanApproval, secretaryApproval, treasurerApproval);
  }

  private void generateLoanOnSuccessfulApproval(LoanApplication application) {
    var loanApprovals = this.retrieveLoanApprovals(application);

    var approved =
        loanApprovals.chairman().status() == LoanApprovalStatus.APPROVED
            && loanApprovals.secretary().status() == LoanApprovalStatus.APPROVED
            && loanApprovals.treasurer().status() == LoanApprovalStatus.APPROVED;

    if (approved) {
      var loan =
          new Loan()
              .member(application.member())
              .amount(application.amount())
              .reasonForLoan(application.reasonForLoan())
              .status(LoanStatus.AWAITING_DISBURSEMENT);

      loanRepository.save(loan);
    }
  }

  private Optional<LoanApplication> retrieveActiveLoanApplication(Member member) {
    return this.loanApplicationRepository.getLoanApplicationByMemberAndApprovalStatus(
        member, LoanApprovalStatus.AWAITING_APPROVAL);
  }

  public List<LoanApplicationVO> retrieveLoanApplications() {
    return this.loanApplicationRepository.findAll().stream()
        .map(
            application -> {
              var approvals = this.retrieveLoanApprovals(application);
              return new LoanApplicationVO(application, approvals);
            })
        .toList();
  }
}
