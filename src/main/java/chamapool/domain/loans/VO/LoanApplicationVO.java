package chamapool.domain.loans.VO;

import chamapool.domain.loans.LoanApplication;
import chamapool.domain.loans.enums.LoanApprovalStatus;

public record LoanApplicationVO(
    Integer loanId, Double amount, LoanApprovalStatus status, String reasonForLoan) {
  public LoanApplicationVO(LoanApplication application) {
    this(
        application.loan().id(),
        application.loan().amount(),
        application.approvalStatus(),
        application.loan().reasonForLoan());
  }
}

