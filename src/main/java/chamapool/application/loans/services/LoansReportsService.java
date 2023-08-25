package chamapool.application.loans.services;

import chamapool.domain.loans.Loan;
import chamapool.domain.loans.VO.LoanInstallmentVO;
import chamapool.domain.loans.VO.LoanVO;
import chamapool.domain.loans.enums.LoanStatus;
import chamapool.domain.loans.repositories.LoanApplicationRepository;
import chamapool.domain.loans.repositories.LoanInstallmentsRepository;
import chamapool.domain.loans.repositories.LoanRepository;
import chamapool.domain.member.models.Member;
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
  private final LoanInstallmentsRepository installmentsRepository;

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
            .filter(loan -> loan.status() != LoanStatus.AWAITING_DISBURSEMENT)
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
    params.put("groupLoansDataset", dataSource);

    JasperReport report =
        JasperCompileManager.compileReport("src/main/resources/reports/group-loans-report.jrxml");
    JasperPrint print = JasperFillManager.fillReport(report, params, new JREmptyDataSource());

    return JasperExportManager.exportReportToPdf(print);
  }

  public byte[] generateLoanReport(Integer loanId) throws JRException {
    var loan = this.loanRepository.getReferenceById(loanId);
    var loanInstallments =
        this.installmentsRepository.findLoanInstallmentsByLoan(loan).stream()
            .map(LoanInstallmentVO::new)
            .toList();

    JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(loanInstallments, false);

    Map<String, Object> params = new HashMap<>();

    params.put("amountPayable", loan.amountPayable());
    params.put("phoneNumber", loan.member().phoneNumber());
    params.put("nationalId", loan.member().nationalId());
    params.put("fullName", loan.member().fullName());
    params.put("balance", loan.balance());
    params.put("interestRate", loan.interestRate());
    params.put("memberId", loan.member().id());
    params.put("dueDate", loan.dueDate());
    params.put("startDate", loan.startDate());
    params.put("amount", loan.amount());
    params.put("loanId", loanId);
    params.put("installmentsDataset", dataSource);

    JasperReport report =
        JasperCompileManager.compileReport("src/main/resources/reports/loan-report.jrxml");
    JasperPrint print = JasperFillManager.fillReport(report, params, dataSource);

    return JasperExportManager.exportReportToPdf(print);
  }

  public byte[] generateMemberLoansReport(Member member) throws JRException {
    var loans = this.loanRepository.getLoansByMember(member).stream().map(LoanVO::new).toList();
    JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(loans, false);

    Map<String, Object> params = new HashMap<>();
    params.put("phoneNumber", member.phoneNumber());
    params.put("nationalId", member.nationalId());
    params.put("memberName", member.fullName());
    params.put("memberId", member.id());
    params.put("totalAmountBorrowed", this.loanRepository.sumAmountBorrowedByMember(member));
    params.put("totalAmountRepaid", this.loanRepository.sumAmountPaidByMember(member));
    params.put("totalLoansBorrowed", this.loanRepository.countByMember(member));

    params.put("personalLoansInstallments", dataSource);

    JasperReport report =
        JasperCompileManager.compileReport(
            "src/main/resources/reports/personal-loans-report.jrxml");
    JasperPrint print = JasperFillManager.fillReport(report, params, new JREmptyDataSource());

    return JasperExportManager.exportReportToPdf(print);
  }
}
