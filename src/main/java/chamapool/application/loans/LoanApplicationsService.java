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
public class LoanApplicationsService {
  private final LoanRepository loanRepository;
  private final LoanApprovalRepository loanApprovalRepository;
  private final LoanApplicationRepository loanApplicationRepository;
  private final MemberRepository memberRepository;

  public List<LoanApplicationVO> retrieveAllApplications() {
    return this.loanApplicationRepository.findAll().stream()
        .map(
            application -> {
              var approvals = this.retrieveLoanApprovals(application);
              return new LoanApplicationVO(application, approvals);
            })
        .toList();
  }

  @Transactional
  public LoanApplicationVO applyForLoan(Member applicant, LoanApplicationRequest req) {
    log.info(
        "Member {} applying for loan of amount: {}",
        applicant.firstName() + ' ' + applicant.lastName(),
        req.amount());

    var existingApplication = this.retrieveActiveApplication(applicant);
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
  public LoanApprovalVO approveApplication(
      Member stakeholder, Integer applicationId, LoanApprovalRequest req) {
    var loanApplication = loanApplicationRepository.getReferenceById(applicationId);

    log.info(
        "{} approving {}", stakeholder.firstName() + ' ' + stakeholder.lastName(), loanApplication);

    var approval =
        loanApprovalRepository
            .getLoanApprovalsByStakeholderAndLoanApplication(stakeholder, loanApplication)
            .orElse(
                new LoanApproval()
                    .id(new LoanApprovalId())
                    .stakeholder(stakeholder)
                    .loanApplication(loanApplication))
            .status(req.approved() ? LoanApprovalStatus.APPROVED : LoanApprovalStatus.REJECTED)
            .message(req.message());

    loanApprovalRepository.save(approval);

    this.generateLoanOnSuccessfulApproval(loanApplication);

    return new LoanApprovalVO(approval);
  }

  public LoanApplicationVO retrieveApplication(Integer applicationId) {
    var loanApplication = this.loanApplicationRepository.getReferenceById(applicationId);
    var approvals = this.retrieveLoanApprovals(loanApplication);
    return new LoanApplicationVO(loanApplication, approvals);
  }

  public LoanApplicationVO retrieveMemberActiveApplication(Member member) {
    var application = this.retrieveActiveApplication(member).orElseThrow();

    return new LoanApplicationVO(application, this.retrieveLoanApprovals(application));
  }

  public LoanEligibilityResponse checkLoanEligibility(Member member) {
    var application = this.retrieveActiveApplication(member);

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

    var awaitingApproval =
        loanApprovals.chairman().status() == LoanApprovalStatus.AWAITING_APPROVAL
            || loanApprovals.secretary().status() == LoanApprovalStatus.AWAITING_APPROVAL
            || loanApprovals.treasurer().status() == LoanApprovalStatus.AWAITING_APPROVAL;

    var approved =
        loanApprovals.chairman().status() == LoanApprovalStatus.APPROVED
            && loanApprovals.secretary().status() == LoanApprovalStatus.APPROVED
            && loanApprovals.treasurer().status() == LoanApprovalStatus.APPROVED;

    var rejected =
        loanApprovals.chairman().status() == LoanApprovalStatus.REJECTED
            || loanApprovals.secretary().status() == LoanApprovalStatus.REJECTED
            || loanApprovals.treasurer().status() == LoanApprovalStatus.REJECTED;

    if (!awaitingApproval && approved) {
      application.approvalStatus(LoanApprovalStatus.APPROVED);
      loanApplicationRepository.save(application);

      var loan =
          new Loan()
              .member(application.member())
              .amount(application.amount())
              .reasonForLoan(application.reasonForLoan())
              .status(LoanStatus.AWAITING_DISBURSEMENT);

      loanRepository.save(loan);
    } else if (!awaitingApproval && rejected) {
      application.approvalStatus(LoanApprovalStatus.REJECTED);
      loanApplicationRepository.save(application);
    }
  }

  private Optional<LoanApplication> retrieveActiveApplication(Member member) {
    return this.loanApplicationRepository.getLoanApplicationByMemberAndApprovalStatus(
        member, LoanApprovalStatus.AWAITING_APPROVAL);
  }
}