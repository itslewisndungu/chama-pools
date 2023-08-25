package chamapool.application.transactions;

import chamapool.domain.chama.ChamaRepository;
import chamapool.domain.transaction.Transaction;
import chamapool.domain.transaction.TransactionRepository;
import chamapool.domain.transaction.TransactionType;
import chamapool.domain.transaction.TransactionVO;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionsService {
  private final TransactionRepository transactionRepository;
  private final ChamaRepository chamaRepository;

  public void createTransaction(TransactionType type, Double amount, String descriptions) {
    log.info("Creating transaction of type {} with amount {} at {}", type, amount, LocalDate.now());

    var transaction = new Transaction().amount(amount).type(type).description(descriptions);
    this.createTransaction(transaction);
  }

  @Transactional
  public void createTransaction(Transaction transaction) {
    log.info(
        "Creating transaction of type {} with amount {} at {}",
        transaction.type(),
        transaction.amount(),
        LocalDate.now());

    this.transactionRepository.save(transaction);
    this.syncGroupAccount(transaction.type(), transaction.amount());
  }

  private void syncGroupAccount(TransactionType type, Double amount) {
    log.info("Syncing group account ...");

    var chama =
        this.chamaRepository
            .getChama()
            .orElseThrow(() -> new RuntimeException("Chama not initialized"));

    switch (type) {
      case WITHDRAWAL, LOAN_DISBURSEMENT, DIVIDEND -> {
        chama.accountBalance(chama.accountBalance() - amount);
      }
      case INVESTMENT_INCOME,
          LOAN_REPAYMENT,
          DEPOSIT,
          MEMBERSHIP_FEE,
          CONTRIBUTION,
          LOAN_INTEREST -> {
        chama.accountBalance(chama.accountBalance() + amount);
      }
    }

    this.chamaRepository.save(chama);
  }

  public List<TransactionVO> retrieveTransactions() {
    return this.transactionRepository.findAll().stream().map(TransactionVO::new).toList();
  }

  public byte[] generateTransactionsReport() throws JRException {
    var tnx = this.transactionRepository.findAll();
    var dataset = tnx.stream().map(TransactionVO::new).toList();

    JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(dataset, false);
    var chama = this.chamaRepository.getChama().orElseThrow();

    var params = new HashMap<String, Object>();

    params.put("accountBalance", chama.accountBalance());
    params.put("incomeRevenue", this.getIncomeRevenue(tnx));
    params.put("expenditureRevenue", this.getExpensesRevenue(tnx));
    params.put("transactionsDataset", dataSource);

    JasperReport report =
        JasperCompileManager.compileReport("src/main/resources/reports/transactions-report.jrxml");

    JasperPrint print = JasperFillManager.fillReport(report, params, new JREmptyDataSource());

    return JasperExportManager.exportReportToPdf(print);
  }

  private double getIncomeRevenue(List<Transaction> transactions) {
    var incomeTransactions =
        List.of(TransactionType.INVESTMENT_INCOME, TransactionType.LOAN_INTEREST);

    return transactions.stream()
        .filter(transaction -> incomeTransactions.contains(transaction.type()))
        .mapToDouble(Transaction::amount)
        .sum();
  }

  private double getExpensesRevenue(List<Transaction> transactions) {

    var expenseTransactions = List.of(TransactionType.DIVIDEND, TransactionType.WITHDRAWAL);

    return transactions.stream()
        .filter(transaction -> expenseTransactions.contains(transaction.type()))
        .mapToDouble(Transaction::amount)
        .sum();
  }
}
