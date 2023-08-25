package chamapool.domain.loans.VO;

import chamapool.domain.loans.LoanApplication;
import chamapool.domain.loans.enums.LoanApprovalStatus;

public record LoanApplicationVO(
    Integer id,
    Double amount,
    Integer memberId,
    String memberName,
    String memberPhoneNumber,
    LoanApprovalStatus status,
    String reasonForLoan,
    Approvals approval) {
  public LoanApplicationVO(LoanApplication application, Approvals approvals) {
    this(
        application.id(),
        application.amount(),
        application.member().id(),
        application.member().fullName(),
        application.member().phoneNumber(),
        application.approvalStatus(),
        application.reasonForLoan(),
        approvals);
  }
}
