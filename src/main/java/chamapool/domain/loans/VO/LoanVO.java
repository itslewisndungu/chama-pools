package chamapool.domain.loans.VO;

import chamapool.domain.loans.Loan;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoanVO {
  private Integer loanId;
  private Integer memberId;
  private String fullName;
  private String nationalId;
  private String phoneNumber;
  private Double amount;
  private Double interestEarned;
  private Double amountPayable;
  private Double balance;
  private String reasonForLoan;
  private Double interestRate;
  private LocalDate startDate;
  private LocalDate dueDate;
  private LocalDate endDate;
  private String status;

  public LoanVO(Loan loan) {
    this(
        loan.id(),
        loan.member().id(),
        loan.member().fullName(),
        loan.member().nationalId(),
        loan.member().phoneNumber(),
        loan.amount(),
        loan.interestEarned(),
        loan.amountPayable(),
        loan.balance(),
        loan.reasonForLoan(),
        loan.interestRate(),
        loan.startDate(),
        loan.dueDate(),
        loan.endDate(),
        loan.status().name());
  }
}
