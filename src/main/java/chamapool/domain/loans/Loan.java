package chamapool.domain.loans;

import jakarta.persistence.*;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Setter
@Getter
@EntityListeners(AuditingEntityListener.class)
@Accessors(chain = true, fluent = true)
public class Loan {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  private Integer memberId;
  private Double amount;
  private Double interestRate;
  private LocalDate startDate;
  private LocalDate endDate;

  @Enumerated(EnumType.STRING)
  private LoanStatus status;
}
