package chamapool.application.loans.requests;

import chamapool.domain.loans.enums.LoanApprovalStatus;

public record LoanApprovalRequest(String message, Boolean approved) {}
