package chamapool.domain.chama;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Setter
@Getter
@Accessors(chain = true, fluent = true)
@EntityListeners(AuditingEntityListener.class)
public class Chama {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "chama_id")
  private Integer chamaId;

  @Column(name = "name", nullable = false)
  private String name;

  @CreatedDate
  @Column(name = "created_at")
  private LocalDate createdAt;

  @Column(name = "account_balance", nullable = false)
  private BigDecimal accountBalance;
}
