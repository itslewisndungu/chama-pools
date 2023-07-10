package chamapool.domain.transaction;

import jakarta.persistence.*;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TransactionVO {
  private Integer id;
  private LocalDate date;
  private TransactionType type;
  private Double amount;
  private String description;

  public TransactionVO(Transaction tnx) {
    this(
        tnx.id(),
        LocalDate.from(tnx.transactionDate()),
        tnx.type(),
        tnx.amount(),
        tnx.description());
  }
}
