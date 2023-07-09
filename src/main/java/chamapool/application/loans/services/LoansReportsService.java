package chamapool.application.loans.services;

import chamapool.domain.loans.Loan;
import chamapool.domain.loans.VO.LoanVO;
import chamapool.domain.loans.enums.LoanStatus;
import chamapool.domain.loans.repositories.LoanApplicationRepository;
import chamapool.domain.loans.repositories.LoanRepository;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoansReportsService {
  private final LoanRepository loanRepository;
  private final LoanApplicationRepository loanApplicationRepository;

  public byte[] generateLoansReport() throws JRException {
    var loans = this.loanRepository.findAll().stream().map(LoanVO::new).toList();
    JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(loans, false);

    var issuedLoans = (int) this.loanRepository.count();
    var activeLoans = this.loanRepository.countByStatus(LoanStatus.ACTIVE);
    var pendingLoans = this.loanRepository.countByStatus(LoanStatus.AWAITING_DISBURSEMENT);
    var repaidLoans = this.loanRepository.countByStatus(LoanStatus.REPAID);
    var overdueLoans = this.loanRepository.countByStatus(LoanStatus.OVERDUE);

    var loanApplications = (int) this.loanApplicationRepository.count();
    var totalOutstandingBalances =
        this.loanRepository.findAll().stream()
            .filter(
                loan -> loan.status() == LoanStatus.ACTIVE || loan.status() == LoanStatus.OVERDUE)
            .mapToDouble(Loan::balance)
            .sum();

    var totalAmountRepaid = this.loanRepository.sumAmountPaid();
    var totalAmountBorrowed = this.loanRepository.sumAmountBorrowed();

    Map<String, Object> params = new HashMap<>();
    params.put("loanApplications", loanApplications);
    params.put("pendingLoans", pendingLoans);
    params.put("repaidLoans", repaidLoans);
    params.put("overdueLoans", overdueLoans);
    params.put("activeLoans", activeLoans);
    params.put("totalOutstandingBalances", totalOutstandingBalances);
    params.put("totalAmountRepaid", totalAmountRepaid);
    params.put("totalAmountBorrowed", totalAmountBorrowed);
    params.put("issuedLoans", issuedLoans);
    params.put("loansDataSet", dataSource);

    JasperReport report =
        JasperCompileManager.compileReport("src/main/resources/reports/group-loans-report.jrxml");
    JasperPrint print = JasperFillManager.fillReport(report, params, new JREmptyDataSource());

    return JasperExportManager.exportReportToPdf(print);
  }
}
