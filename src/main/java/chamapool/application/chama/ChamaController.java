package chamapool.application.chama;

import chamapool.domain.chama.ChamaVO;
import java.util.HashMap;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chama")
public class ChamaController {
  private final ChamaService chamaService;

  @GetMapping
  public ChamaVO getChamaDetails() {
    return this.chamaService.getChamaDetails();
  }

  @GetMapping("/members-summary")
  public HashMap<String, Integer> getMemberSummary() {
    return this.chamaService.getMemberSummary();
  }

  @GetMapping("/loans-summary")
  public HashMap<String, Integer> getLoanSummary() {
    return this.chamaService.getLoanSummary();
  }

  @GetMapping("/account-summary")
  public void getAccountSummary() {
    this.chamaService.getAccountSummary();
  }

  @GetMapping("/meetings-summary")
  public void getMeetingSummary() {
    this.chamaService.getMeetingsSummary();
  }
}
