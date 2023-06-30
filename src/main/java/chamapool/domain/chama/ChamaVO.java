package chamapool.domain.chama;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ChamaVO(
    Integer chamaId, String name, LocalDate createdAt, BigDecimal accountBalance) {
  public ChamaVO(Chama chama) {
    this(chama.chamaId(), chama.name(), chama.createdAt(), chama.accountBalance());
  }
}
