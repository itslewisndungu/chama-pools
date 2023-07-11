package chamapool.application.chama;

import chamapool.domain.chama.ChamaVO;
import java.util.HashMap;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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
  public HashMap<String, Object> getLoanSummary() {
    return this.chamaService.getLoanSummary();
  }

  @GetMapping("/account-summary")
  public HashMap<String, Double> getAccountSummary() {
    return this.chamaService.getAccountSummary();
  }

  @GetMapping("/meetings-summary")
  public HashMap<String, Object> getMeetingSummary() {
    return this.chamaService.getMeetingsSummary();
  }

  @PostMapping("/disburse-dividends")
  @ResponseStatus(HttpStatus.CREATED)
  public void disburseDividends(@RequestBody DisburseDividendRequest request) {
    this.chamaService.disburseDividends(request);
  }

  @PostMapping("record-income")
  @ResponseStatus(HttpStatus.CREATED)
  public void recordIncome(@RequestBody RecordIncomeRequest request) {
    this.chamaService.recordIncome(request);
  }

  @PostMapping("record-withdrawal")
  @ResponseStatus(HttpStatus.CREATED)
  public void recordWithdrawal(@RequestBody RecordWithdrawalRequest request) {
    this.chamaService.recordWithdrawal(request);
  }
}
