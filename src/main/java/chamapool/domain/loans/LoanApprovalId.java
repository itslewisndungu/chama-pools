package chamapool.domain.loans;

import jakarta.persistence.Embeddable;
import java.io.Serial;
import java.io.Serializable;
import lombok.Data;

@Data
@Embeddable
public class LoanApprovalId implements Serializable {
  @Serial private static final long serialVersionUID = 1L;

  private Integer loanApplicationId;
  private Integer stakeholderId;
}
