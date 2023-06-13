package chamapool.domain.loans;

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
public class LoanRequest {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @OneToOne
  @JoinColumn(name = "loan_id")
  private Loan loan;

  @Enumerated(EnumType.STRING)
  private LoanRequestStatus status = LoanRequestStatus.AWAITING_APPROVAL;

  @OneToOne
  @JoinColumn(name = "chairman_id")
  private Member chairman;

  @OneToOne
  @JoinColumn(name = "secretary_id")
  private Member secretary;

  @OneToOne
  @JoinColumn(name = "treasurer_id")
  private Member treasurer;
}
