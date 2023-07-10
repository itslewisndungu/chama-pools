package chamapool.application.reports;

import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.JRException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/reports")
public class ReportsController {
  private final ReportsService reportsService;

  @GetMapping("/group-members")
  public ResponseEntity<byte[]> getMembersReport() {
    try {
      var pdf = this.reportsService.generateMembersReport();

      return ResponseEntity.ok()
          .header("Content-Type", "application/pdf")
          .header("Content-Disposition", "inline; filename=group-members.pdf")
          .body(pdf);
    } catch (JRException e) {
      e.printStackTrace();
      return ResponseEntity.badRequest().build();
    }
  }

  @GetMapping("/group-contributions")
  public ResponseEntity<byte[]> getContributionsReport() {
    try {
      var pdf = this.reportsService.generateContributionsReport();

      return ResponseEntity.ok()
              .header("Content-Type", "application/pdf")
              .header("Content-Disposition", "inline; filename=group-contributions.pdf")
              .body(pdf);
    } catch (JRException e) {
      e.printStackTrace();
      return ResponseEntity.badRequest().build();
    }
  }
}
