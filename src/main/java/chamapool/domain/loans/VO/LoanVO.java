package chamapool.domain.loans.VO;

import chamapool.domain.loans.Loan;
import chamapool.domain.loans.enums.LoanStatus;
import java.time.LocalDate;

public record LoanVO(
    Integer id,
    Integer memberId,
    String memberName,
    Double amount,
    Double interestEarned,
    Double amountPayable,
    String reasonForLoan,
    Double interestRate,
    LocalDate startDate,
    LocalDate dueDate,
    LocalDate endDate,
    LoanStatus status) {
  public LoanVO(Loan loan) {
    this(
        loan.id(),
        loan.member().id(),
        loan.member().fullName(),
        loan.amount(),
        loan.interestEarned(),
        loan.amountPayable(),
        loan.reasonForLoan(),
        loan.interestRate(),
        loan.startDate(),
        loan.dueDate(),
        loan.endDate(),
        loan.status());
  }
}
