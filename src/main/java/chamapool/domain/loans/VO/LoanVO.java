package chamapool.domain.loans.VO;

import chamapool.domain.loans.Loan;
import chamapool.domain.loans.enums.LoanStatus;
import java.time.LocalDate;

public record LoanVO(
    Integer id,
    Integer memberId,
    Double amount,
    String reasonForLoan,
    Double interestRate,
    LocalDate startDate,
    LocalDate endDate,
    LocalDate applicationDate,
    LoanStatus status) {
  public LoanVO(Loan loan) {
    this(
        loan.id(),
        loan.member().id(),
        loan.amount(),
        loan.reasonForLoan(),
        loan.interestRate(),
        loan.startDate(),
        loan.endDate(),
        loan.applicationDate(),
        loan.status());
  }
}
