package chamapool.domain.loans.VO;

import chamapool.domain.loans.Loan;
import chamapool.domain.loans.LoanApproval;
import chamapool.domain.loans.enums.LoanApprovalStatus;

public record LoanApprovalVO(Integer loanId, LoanApprovalStatus approvalStatus, String message) {
  public LoanApprovalVO(LoanApproval approval) {
    this(approval.loanApplication().loan().id(), approval.status(), approval.message());
  }
}
