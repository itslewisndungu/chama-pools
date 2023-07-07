package chamapool.domain.loans.VO;

import chamapool.domain.loans.LoanInstallment;
import java.time.LocalDate;

public record LoanInstallmentVO(Integer id, Integer loanId, Double amount, LocalDate date) {
  public LoanInstallmentVO(LoanInstallment installment) {
    this(installment.id(), installment.loan().id(), installment.amount(), installment.date());
  }
}
