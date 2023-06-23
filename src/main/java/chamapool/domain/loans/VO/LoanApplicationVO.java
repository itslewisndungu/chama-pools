package chamapool.domain.loans.VO;

import chamapool.domain.loans.LoanApplication;
import chamapool.domain.loans.enums.LoanApprovalStatus;

public record LoanApplicationVO(
    Double amount, LoanApprovalStatus status, String reasonForLoan, Approvals approval) {
  public LoanApplicationVO(LoanApplication application, Approvals approvals) {
    this(
        application.loan().amount(),
        application.approvalStatus(),
        application.loan().reasonForLoan(),
        approvals);
  }
}

/*
 * {
 * "amount": 1000,
 * "status": "APPROVED",
 * "reasonForLoan": "I need to buy a new laptop",
 * "approvals": {
 * "chairman": {
 * "status": "APPROVED",
 * "message": "Looks good to me"
 * },
 *"secretary": {
 * "status": "REJECTED",
 * "message": "I don't think you need a new laptop",
 *},
 * "treasurer": {
 * "status": "APPROVED",
 * "message": "I think you need a new laptop"
 * }
 * }
 * */
