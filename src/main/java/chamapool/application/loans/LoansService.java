package chamapool.application.loans;

import chamapool.application.notifications.NotificationsService;
import chamapool.application.transactions.TransactionsService;
import chamapool.domain.loans.Loan;
import chamapool.domain.loans.LoanInstallment;
import chamapool.domain.loans.VO.*;
import chamapool.domain.loans.enums.LoanStatus;
import chamapool.domain.loans.repositories.LoanRepaymentRepository;
import chamapool.domain.loans.repositories.LoanRepository;
import chamapool.domain.member.models.Member;
import chamapool.domain.notifications.models.Notification;
import chamapool.domain.notifications.models.NotificationType;
import chamapool.domain.transaction.TransactionType;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoansService {
  private final LoanRepository loanRepository;
  private final TransactionsService transactionsService;
  private final LoanRepaymentRepository loanRepaymentRepository;

  private final NotificationsService notificationsService;

  public LoanVO retrieveLoan(Integer loanId) {
    var loan = this.loanRepository.getReferenceById(loanId);
    return new LoanVO(loan);
  }

  public List<LoanVO> retrieveMemberLoans(Member member) {
    return this.loanRepository.getLoansByMember(member).stream().map(LoanVO::new).toList();
  }

  @Transactional
  public LoanVO disburseLoan(Integer loanId) {
    var loan = this.loanRepository.getReferenceById(loanId);
    log.info("Disbursing loan with ID {} and worth {}", loanId, loan.amount());

    loan.startDate(LocalDate.now());
    loan = this.loanRepository.save(loan);

    this.updateLoanStatus(loan, LoanStatus.ACTIVE);

    this.transactionsService.createTransaction(TransactionType.LOAN_DISBURSEMENT, loan.amount());

    var loanDisbursementNotification =
        new Notification()
            .title("Loan Disbursement")
            .relatedId(loan.id())
            .message("Your loan of amount %.2f has been disbursed".formatted(loan.amount()))
            .type(NotificationType.LOAN_DISBURSEMENT);

    // Can we do this on another thread?
    this.notificationsService.sendMemberNotification(loan.member(), loanDisbursementNotification);

    return new LoanVO(loan);
  }

  @Transactional
  public LoanInstallmentVO payLoanInstallment(Integer loanId, Double amount) {
    var loan = this.loanRepository.getReferenceById(loanId);

    if (loan.status() == LoanStatus.REPAID) {
      throw new RuntimeException("Loan has already been repaid");
    } else if (loan.status() == LoanStatus.AWAITING_DISBURSEMENT) {
      throw new RuntimeException("Loan has not been disbursed yet");
    }

    log.info("Paying installment for loan with ID {} and worth {}", loanId, amount);

    var loanRepayment = new LoanInstallment().loan(loan).amount(amount);
    this.loanRepaymentRepository.save(loanRepayment);

    loan.amountPaid(loan.amountPaid() + amount);
    loan.repayments().add(loanRepayment);
    if (loan.balance() <= 0) loan.endDate(LocalDate.now());

    loan = this.loanRepository.save(loan);
    this.updateLoanStatus(loan);

    this.transactionsService.createTransaction(TransactionType.LOAN_REPAYMENT, amount * 0.9);
    this.transactionsService.createTransaction(TransactionType.LOAN_INTEREST, amount * 0.1);

    var loanRepaymentNotification =
        new Notification()
            .title("Loan Repayment")
            .type(NotificationType.LOAN_REPAYMENT)
            .relatedId(loan.id())
            .message(
                "An installment of %.2f has been received for your loan. Your new outstanding balance is %.2f"
                    .formatted(amount, loan.balance()));

    this.notificationsService.sendMemberNotification(loan.member(), loanRepaymentNotification);
    return new LoanInstallmentVO(loanRepayment);
  }

  private void updateLoanStatus(Loan loan) {
    var loanOverdue = LocalDate.now().isAfter(loan.dueDate());
    var loanFullyPaid = loan.balance() == 0 && Objects.equals(loan.amountPayable(), loan.amountPaid());
    var loanActive = !loanOverdue && loan.balance() > 0;

    if (loanFullyPaid) {
      this.updateLoanStatus(loan, LoanStatus.REPAID);
    } else if (loanOverdue) {
      this.updateLoanStatus(loan, LoanStatus.OVERDUE);
    } else if (loanActive) {
      this.updateLoanStatus(loan, LoanStatus.ACTIVE);
    }
  }

  private void updateLoanStatus(Loan loan, LoanStatus status) {
    loan.status(status);
    this.loanRepository.save(loan);
  }

  public List<LoanVO> retrieveAllLoans() {
    return this.loanRepository.findAll().stream().map(LoanVO::new).collect(Collectors.toList());
  }

  public List<LoanInstallmentVO> retrieveLoanInstallments(Integer loanId) {
    var loan = this.loanRepository.getReferenceById(loanId);
    return loan.repayments().stream().map(LoanInstallmentVO::new).collect(Collectors.toList());
  }
}
