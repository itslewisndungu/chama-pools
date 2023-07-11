package chamapool.application.reports;

import chamapool.application.chama.AccountService;
import chamapool.application.chama.ChamaService;
import chamapool.domain.chama.ChamaRepository;
import chamapool.domain.meeting.MeetingContributionVO;
import chamapool.domain.meeting.repositories.MeetingContributionRepository;
import chamapool.domain.member.VOs.MemberVO;
import chamapool.domain.member.repositories.MemberRepository;
import chamapool.domain.transaction.TransactionRepository;
import chamapool.domain.transaction.TransactionVO;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReportsService {
  private final MemberRepository memberRepository;
  private final MeetingContributionRepository contributionRepository;
  private final TransactionRepository tnxRepository;
  private final ChamaRepository chamaRepository;
  private final ChamaService chamaService;
  private final AccountService accountService;

  public byte[] generateMembersReport() throws JRException {
    var members = this.memberRepository.findAll().stream().map(MemberVO::new).toList();

    JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(members, false);

    Map<String, Object> params = new HashMap<>();
    params.put("groupMembers", dataSource);

    JasperReport report =
        JasperCompileManager.compileReport("src/main/resources/reports/group-members.jrxml");

    JasperPrint print = JasperFillManager.fillReport(report, params, new JREmptyDataSource());

    return JasperExportManager.exportReportToPdf(print);
  }

  public byte[] generateContributionsReport() throws JRException {
    var contributionsPerMember =
        this.contributionRepository.getMeetingContributionsGroupByMember().stream()
            .map(MeetingContributionVO::new)
            .toList();

    JRBeanCollectionDataSource dataSource =
        new JRBeanCollectionDataSource(contributionsPerMember, false);

    var totalContributionsSum = this.contributionRepository.getTotalContributionsSum();

    var params = new HashMap<String, Object>();
    params.put("groupContributionsPerMember", dataSource);
    params.put("totalAmountContributed", totalContributionsSum);

    JasperReport report =
        JasperCompileManager.compileReport("src/main/resources/reports/group-contributions.jrxml");

    JasperPrint print = JasperFillManager.fillReport(report, params, new JREmptyDataSource());

    return JasperExportManager.exportReportToPdf(print);
  }

  public byte[] generateTransactionsReport() throws JRException {
    var tnx = this.tnxRepository.findAll();
    var dataset = tnx.stream().map(TransactionVO::new).toList();

    JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(dataset, false);
    var chama = this.chamaRepository.getChama().orElseThrow();

    var params = new HashMap<String, Object>();

    params.put("accountBalance", chama.accountBalance());
    params.put("incomeRevenue", this.accountService.getIncomeRevenue(tnx));
    params.put("expenditureRevenue", this.accountService.getExpensesRevenue(tnx));
    params.put("transactionsDataset", dataSource);

    JasperReport report =
        JasperCompileManager.compileReport("src/main/resources/reports/transactions-report.jrxml");

    JasperPrint print = JasperFillManager.fillReport(report, params, new JREmptyDataSource());

    return JasperExportManager.exportReportToPdf(print);
  }
}
/*
   <parameter name="accountBalance" class="java.lang.Double"/>
   <parameter name="incomeRevenue" class="java.lang.Double"/>
   <parameter name="expenditureRevenue" class="java.lang.Double"/>
   <parameter name="transactionsDataset" class="net.sf.jasperreports.engine.data.JRBeanCollectionDataSource"/>
*/
