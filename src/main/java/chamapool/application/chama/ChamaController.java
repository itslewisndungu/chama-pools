package chamapool.application.chama;

import chamapool.domain.chama.ChamaVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chama")
public class ChamaController {
  private final ChamaService chamaService;

  @GetMapping()
  public ChamaVO getChamaDetails() {
    return this.chamaService.getChamaDetails();
  }
}
