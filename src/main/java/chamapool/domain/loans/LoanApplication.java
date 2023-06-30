package chamapool.domain.loans;

import chamapool.domain.loans.enums.LoanApprovalStatus;
import chamapool.domain.member.models.Member;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Setter
@Getter
@EntityListeners(AuditingEntityListener.class)
@Accessors(chain = true, fluent = true)
public class LoanApplication {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private Integer id;

  @ManyToOne
  @JoinColumn(name = "member_id")
  private Member member;

  @CreatedDate
  private LocalDate applicationDate;

  private Double amount;

  private String reasonForLoan;

  @Enumerated(EnumType.STRING)
  private LoanApprovalStatus approvalStatus = LoanApprovalStatus.AWAITING_APPROVAL;

  @OneToMany(mappedBy = "loanApplication")
  private List<LoanApproval> loanApproval;
}
