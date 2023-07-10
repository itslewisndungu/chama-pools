package chamapool.application.transactions;

import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.JRException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/transactions")
@RequiredArgsConstructor
public class TransactionsReportsController {
  private final TransactionsService service;

  @GetMapping("/report")
  public ResponseEntity<byte[]> getTransactionsReport() {
    try {
      var report = this.service.generateTransactionsReport();

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
