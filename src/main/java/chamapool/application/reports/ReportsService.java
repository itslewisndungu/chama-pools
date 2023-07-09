package chamapool.application.reports;

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
}
