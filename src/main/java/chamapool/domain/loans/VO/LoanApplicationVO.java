package chamapool.domain.loans.VO;

import chamapool.domain.loans.LoanApplication;
import chamapool.domain.loans.enums.LoanApprovalStatus;

public record LoanApplicationVO(
    Double amount, LoanApprovalStatus status, String reasonForLoan, Approvals approval) {
  public LoanApplicationVO(LoanApplication application, Approvals approvals) {
    this(
        application.amount(),
        application.approvalStatus(),
        application.reasonForLoan(),
        approvals);
  }
}
