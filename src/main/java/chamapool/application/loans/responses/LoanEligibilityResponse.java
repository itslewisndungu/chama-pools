package chamapool.application.loans.responses;

public record LoanEligibilityResponse(boolean isEligible, Integer amountEligible, String reason) {}
