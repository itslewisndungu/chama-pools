package chamapool.application.reports;

import chamapool.domain.meeting.MeetingContributionVO;
import chamapool.domain.meeting.repositories.MeetingContributionRepository;
import chamapool.domain.member.VOs.MemberVO;
import chamapool.domain.member.repositories.MemberRepository;
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
    var contributionsPerMember = this.contributionRepository.getMeetingContributionsGroupByMember()
            .stream().map(MeetingContributionVO::new).toList();

    JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(contributionsPerMember, false);

    var totalContributionsSum = this.contributionRepository.getTotalContributionsSum();

    var params = new HashMap<String, Object>();
    params.put("groupContributionsPerMember", dataSource);
    params.put("totalAmountContributed", totalContributionsSum);

    JasperReport report =
        JasperCompileManager.compileReport("src/main/resources/reports/group-contributions.jrxml");

    JasperPrint print = JasperFillManager.fillReport(report, params, new JREmptyDataSource());

    return JasperExportManager.exportReportToPdf(print);
  }
}
