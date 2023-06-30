package chamapool.domain.chama;

import java.time.LocalDate;

public record ChamaVO(
    Integer chamaId, String name, LocalDate createdAt, Double accountBalance) {
  public ChamaVO(Chama chama) {
    this(chama.chamaId(), chama.name(), chama.createdAt(), chama.accountBalance());
  }
}
