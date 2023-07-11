package chamapool.application.loans.responses;

public record LoanEligibilityResponse(boolean isEligible, Double amountEligible, String reason) {}
