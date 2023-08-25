package chamapool.application.loans.controllers;

import chamapool.application.loans.services.LoansReportsService;
import chamapool.domain.member.models.Member;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.JRException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/loans")
@RequiredArgsConstructor
public class LoansReportsController {
  private final LoansReportsService reportsService;

  @GetMapping("/report")
  public ResponseEntity<byte[]> getLoansReports() {
    try {
      var report = this.reportsService.generateLoansReport();
      return ResponseEntity.ok()
              .header("Content-Type", "application/pdf")
              .header("Content-Disposition", "inline; filename=LoansReport.pdf")
              .body(report);
    } catch (JRException e) {
      e.printStackTrace();
      return ResponseEntity.badRequest().build();
    }
  }

  @GetMapping("/{loanId}/report")
  public ResponseEntity<byte[]> getLoanReports(@PathVariable Integer loanId) {
    try {
      var report = this.reportsService.generateLoanReport(loanId);
      return ResponseEntity.ok()
          .header("Content-Type", "application/pdf")
          .header("Content-Disposition", "inline; filename=Loan" + loanId + "Report.pdf")
          .body(report);
    } catch (JRException e) {
      e.printStackTrace();
      return ResponseEntity.badRequest().build();
    }
  }

  @GetMapping("/member/report")
  public ResponseEntity<byte[]> getMemberLoansReports(Member member) {
    try {
      var report = this.reportsService.generateMemberLoansReport(member);
      return ResponseEntity.ok()
              .header("Content-Type", "application/pdf")
              .header("Content-Disposition", "inline; filename=MemberLoansReport.pdf")
              .body(report);
    } catch (JRException e) {
      e.printStackTrace();
      return ResponseEntity.badRequest().build();
    }
  }
}

