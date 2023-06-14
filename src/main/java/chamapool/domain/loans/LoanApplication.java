package chamapool.domain.loans;

import chamapool.domain.loans.enums.LoanApprovalStatus;
import jakarta.persistence.*;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Setter
@Getter
@EntityListeners(AuditingEntityListener.class)
@Accessors(chain = true, fluent = true)
public class LoanApplication {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @OneToOne
  @JoinColumn(name = "loan_id")
  private Loan loan;

  private LoanApprovalStatus approvalStatus;

  @OneToMany(mappedBy = "loanApplication")
  private List<LoanApproval> loanApproval;
}
