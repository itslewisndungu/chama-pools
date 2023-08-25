package chamapool.application.chama;

import java.util.List;

public record RecordWithdrawalRequest(List<Withdrawal> withdrawals) {}

record Withdrawal(Double amount, String description) {}
