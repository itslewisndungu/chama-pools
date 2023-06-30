package chamapool.application.loans;

import chamapool.application.transactions.TransactionsService;
import chamapool.domain.loans.LoanRepayment;
import chamapool.domain.loans.VO.*;
import chamapool.domain.loans.enums.LoanStatus;
import chamapool.domain.loans.repositories.LoanRepaymentRepository;
import chamapool.domain.loans.repositories.LoanRepository;
import chamapool.domain.member.models.Member;
import chamapool.domain.transaction.TransactionType;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
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

  public LoanVO retrieveLoan(Integer loanId) {
    var loan = this.loanRepository.getReferenceById(loanId);
    return new LoanVO(loan);
  }

  public List<LoanVO> retrieveMemberLoans(Member member) {
    return this.loanRepository.getLoansByMember(member).stream().map(LoanVO::new).toList();
  }

  public void disburseLoan(Integer loanId) {
    var loan = this.loanRepository.getReferenceById(loanId);
    loan.startDate(LocalDate.now()).status(LoanStatus.ACTIVE);
    this.loanRepository.save(loan);
  }

  @Transactional
  public LoanVO payLoanInstallment(Integer loanId, Double amount) {
    var loan = this.loanRepository.getReferenceById(loanId);

    if (loan.status() == LoanStatus.REPAID) {
      throw new RuntimeException("Loan has already been repaid");
    } else if (loan.status() == LoanStatus.AWAITING_DISBURSEMENT) {
      throw new RuntimeException("Loan has not been disbursed yet");
    }

    log.info("Paying installment for loan with ID {} and worth {}", loanId, amount);

    var loanRepayment = new LoanRepayment().loan(loan).amount(amount);
    this.loanRepaymentRepository.save(loanRepayment);

    loan.amountPaid(loan.amountPaid() + amount);
    loan.repayments().add(loanRepayment);
    loan = this.loanRepository.save(loan);

    this.transactionsService.createTransaction(TransactionType.LOAN_REPAYMENT, amount);
    return new LoanVO(loan);
  }
}
