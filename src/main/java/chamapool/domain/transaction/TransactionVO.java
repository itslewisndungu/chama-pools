package chamapool.domain.transaction;

import java.time.LocalDate;
import java.time.ZoneOffset;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TransactionVO {
  private Integer id;
  private LocalDate date;
  private String type;
  private Double amount;
  private String description;

  public TransactionVO(Transaction tnx) {
    this(
        tnx.id(),
        LocalDate.ofInstant(tnx.transactionDate(), ZoneOffset.UTC),
        tnx.type().toString(),
        tnx.amount(),
        tnx.description());
  }
}
