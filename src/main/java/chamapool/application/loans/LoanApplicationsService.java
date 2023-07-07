package chamapool.application.loans;

import chamapool.application.loans.requests.LoanApplicationRequest;
import chamapool.application.loans.requests.LoanApprovalRequest;
import chamapool.application.loans.responses.LoanEligibilityResponse;
import chamapool.application.notifications.NotificationsService;
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
import chamapool.domain.member.enums.MembershipFeeStatus;
import chamapool.domain.member.models.Member;
import chamapool.domain.member.repositories.MemberRepository;
import chamapool.domain.member.repositories.MembershipFeeRepository;
import chamapool.domain.notifications.models.Notification;
import chamapool.domain.notifications.models.NotificationType;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
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
  private final NotificationsService notificationsService;
  private final MembershipFeeRepository membershipFeeRepository;

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

    this.notificationsService.sendAdminNotification(
        new Notification()
            .type(NotificationType.LOAN_APPLICATION)
            .relatedId(application.id())
            .title("New Loan Application")
            .message(
                String.format(
                    "Member %s has applied for a loan of amount %.2f. Please review the application",
                    applicant.fullName(), req.amount())));

    this.notificationsService.sendMemberNotification(
        applicant,
        new Notification()
            .type(NotificationType.LOAN_APPLICATION)
            .relatedId(application.id())
            .title("Loan Application Received")
            .message(
                String.format(
                    "Your loan application of amount %.2f has been received. Please wait for approval",
                    req.amount())));

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
    var activeLoan = this.getActiveMemberLoan(member);
    var membershipFee = this.membershipFeeRepository.getByMember(member);

    if (membershipFee.status() != MembershipFeeStatus.PAID) {
      return new LoanEligibilityResponse(
          false,
          null,
          "You have not paid your membership fee fully. You have an outstanding balance of KES "
              + membershipFee.balance()
              + ".");
    }

    if (application.isPresent()) {
      return new LoanEligibilityResponse(false, null, "You have an active loan application");
    }

    if (activeLoan.isPresent()) {
      return new LoanEligibilityResponse(
          false, null, "You have an active loan. Please pay up first");
    }

    return new LoanEligibilityResponse(true, 100000, null);
  }

  private Optional<Loan> getActiveMemberLoan(Member member) {
    return this.loanRepository.getLoansByMember(member).stream()
        .filter(
            loan ->
                loan.status() == LoanStatus.ACTIVE
                    || loan.status() == LoanStatus.OVERDUE
                    || loan.status() == LoanStatus.AWAITING_DISBURSEMENT)
        .findFirst();
  }

  private Approvals retrieveLoanApprovals(LoanApplication loanApplication) {
    var chairman = this.memberRepository.findChairman().orElseThrow();
    var secretary = this.memberRepository.findSecretary().orElseThrow();
    var treasurer = this.memberRepository.findTreasurer().orElseThrow();

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

    var approvals =
        List.of(loanApprovals.chairman(), loanApprovals.treasurer(), loanApprovals.secretary());

    var awaitingApproval =
        approvals.stream()
            .anyMatch(approval -> approval.status() == LoanApprovalStatus.AWAITING_APPROVAL);

    var approved =
        approvals.stream().allMatch(approval -> approval.status() == LoanApprovalStatus.APPROVED);

    var rejected =
        !awaitingApproval
            && approvals.stream()
                .anyMatch(approval -> approval.status() == LoanApprovalStatus.REJECTED);

    var loanNotification =
        new Notification().type(NotificationType.LOAN_APPLICATION).relatedId(application.id());

    if (approved) {
      application.approvalStatus(LoanApprovalStatus.APPROVED);
      loanApplicationRepository.save(application);

      var loan =
          new Loan()
              .member(application.member())
              .amount(application.amount())
              .reasonForLoan(application.reasonForLoan())
              .interestRate(10.0)
              .startDate(LocalDate.now());

      loanRepository.save(loan);

      loanNotification
          .title("Loan Approved")
          .message(
              "Your loan request of %.2f has been approved. Please pick your cheque in the next meeting"
                  .formatted(application.amount()));

    } else if (rejected) {
      application.approvalStatus(LoanApprovalStatus.REJECTED);
      loanApplicationRepository.save(application);

      loanNotification
          .title("Loan Rejected")
          .message(
              "Your loan of %.2f has been rejected. View loan application for more details"
                  .formatted(application.amount()));
    }

    this.notificationsService.sendMemberNotification(application.member(), loanNotification);
  }

  private Optional<LoanApplication> retrieveActiveApplication(Member member) {
    return this.loanApplicationRepository.getLoanApplicationByMemberAndApprovalStatus(
        member, LoanApprovalStatus.AWAITING_APPROVAL);
  }
}
