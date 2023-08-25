package chamapool.domain.loans.VO;

import chamapool.domain.loans.LoanInstallment;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoanInstallmentVO {
  private Integer id;
  private Integer loanId;
  private Double amount;
  private LocalDate date;

  public LoanInstallmentVO(LoanInstallment installment) {
    this(installment.id(), installment.loan().id(), installment.amount(), installment.date());
  }
}
