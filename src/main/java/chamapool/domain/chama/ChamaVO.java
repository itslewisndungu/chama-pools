package chamapool.domain.chama;

import java.time.LocalDate;

public record ChamaVO(Integer chamaId, String name, LocalDate createdAt) {
  public ChamaVO(Chama chama) {
    this(chama.chamaId(), chama.name(), chama.createdAt());
  }
}
