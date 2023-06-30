package chamapool.application.chama;

import chamapool.domain.chama.ChamaRepository;
import chamapool.domain.chama.ChamaVO;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChamaService {
  private final ChamaRepository chamaRepository;

  public ChamaVO getChamaDetails() {
    return this.chamaRepository
        .getChama()
        .map(ChamaVO::new)
        .orElseThrow(() -> new NoSuchElementException("Chama not found"));
  }
}
