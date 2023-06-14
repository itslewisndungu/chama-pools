package chamapool.domain.loans;

import chamapool.domain.loans.enums.LoanApprovalStatus;
import chamapool.domain.member.models.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Setter
@Getter
@EntityListeners(AuditingEntityListener.class)
@Accessors(chain = true, fluent = true)
public class LoanApproval {
  @EmbeddedId private LoanApprovalId id = new LoanApprovalId();

  @Enumerated(EnumType.STRING)
  private LoanApprovalStatus status;

  private String message;

  @ManyToOne
  @MapsId("loanApplicationId")
  @JoinColumn(name = "loan_application_id")
  private LoanApplication loanApplication;

  @ManyToOne
  @MapsId("stakeholderId")
  @JoinColumn(name = "stakeholder_id")
  private Member stakeholder;
}
