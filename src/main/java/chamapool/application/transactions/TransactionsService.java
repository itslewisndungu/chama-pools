package chamapool.application.transactions;

import chamapool.domain.chama.ChamaRepository;
import chamapool.domain.transaction.Transaction;
import chamapool.domain.transaction.TransactionRepository;
import chamapool.domain.transaction.TransactionType;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionsService {
  private final TransactionRepository transactionRepository;
  private final ChamaRepository chamaRepository;

  @Transactional
  public void createTransaction(TransactionType type, Double amount) {
    log.info("Creating transaction of type {} with amount {} at {}", type, amount, LocalDate.now());

    var transaction = new Transaction().amount(amount).type(type);

    this.transactionRepository.save(transaction);
    this.syncGroupAccount(type, amount);
  }

  private void syncGroupAccount(TransactionType type, Double amount) {
    log.info("Syncing group account ...");

    var chama =
        this.chamaRepository
            .getChama()
            .orElseThrow(() -> new RuntimeException("Chama not initialized"));

    switch (type) {
      case WITHDRAWAL, LOAN_DISBURSEMENT -> {
        chama.accountBalance(chama.accountBalance() - amount);
      }
      case DEPOSIT, LOAN_REPAYMENT, MEMBERSHIP_FEE, CONTRIBUTION, LOAN_INTEREST -> {
        chama.accountBalance(chama.accountBalance() + amount);
      }
    }

    this.chamaRepository.save(chama);
  }
}
