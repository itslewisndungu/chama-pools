package chamapool.application.loans.controllers;

import chamapool.application.loans.services.LoansReportsService;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.JRException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/reports")
@RequiredArgsConstructor
public class LoansReportsController {
  private final LoansReportsService reportsService;

  @GetMapping("/loans")
  public ResponseEntity<byte[]> getLoansReports() {
    try {
      var report = this.reportsService.generateLoansReport();
      return ResponseEntity.ok()
              .header("Content-Type", "application/pdf")
              .header("Content-Disposition", "inline; filename=group-contributions.pdf")
              .body(report);
    } catch (JRException e) {
      e.printStackTrace();
      return ResponseEntity.badRequest().build();
    }
  }

  @GetMapping("/loans/{loanId}")
  public ResponseEntity<byte[]> getLoanReports(@PathVariable Integer loanId) {
    try {
      var report = this.reportsService.generateLoanReport(loanId);
      return ResponseEntity.ok()
          .header("Content-Type", "application/pdf")
          .header("Content-Disposition", "inline; filename=group-contributions.pdf")
          .body(report);
    } catch (JRException e) {
      e.printStackTrace();
      return ResponseEntity.badRequest().build();
    }
  }
}
