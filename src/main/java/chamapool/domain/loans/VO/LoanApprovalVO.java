package chamapool.domain.loans.VO;

import chamapool.domain.loans.LoanApproval;
import chamapool.domain.loans.enums.LoanApprovalStatus;

public record LoanApprovalVO(Integer loanApplicationId, LoanApprovalStatus approvalStatus, String message) {
  public LoanApprovalVO(LoanApproval approval) {
    this(approval.loanApplication().id(), approval.status(), approval.message());
  }
}
