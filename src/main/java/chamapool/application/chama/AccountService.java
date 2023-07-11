package chamapool.application.chama;

import chamapool.domain.transaction.Transaction;
import chamapool.domain.transaction.TransactionType;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountService {
  public double getIncomeRevenue(List<Transaction> transactions) {
    var incomeTransactions =
        List.of(
            TransactionType.INVESTMENT_INCOME,
            TransactionType.LOAN_INTEREST,
            TransactionType.CONTRIBUTION,
            TransactionType.MEMBERSHIP_FEE);

    return transactions.stream()
        .filter(transaction -> incomeTransactions.contains(transaction.type()))
        .mapToDouble(Transaction::amount)
        .sum();
  }

  public double getExpensesRevenue(List<Transaction> transactions) {

    var expenseTransactions = List.of(TransactionType.DIVIDEND, TransactionType.WITHDRAWAL);
    return transactions.stream()
        .filter(transaction -> expenseTransactions.contains(transaction.type()))
        .mapToDouble(Transaction::amount)
        .sum();
  }
}
