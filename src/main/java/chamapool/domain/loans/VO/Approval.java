package chamapool.domain.loans.VO;

import chamapool.domain.loans.enums.LoanApprovalStatus;

public record Approval(LoanApprovalStatus status, String message) {}
