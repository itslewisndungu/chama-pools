package chamapool.domain.loans;

import chamapool.domain.member.models.Member;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
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
public class Loan {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  private Double amount;
  private String reasonForLoan;
  private Double interestRate = 10.0;
  private LocalDate startDate;
  private LocalDate endDate;

  @CreatedDate
  private LocalDate applicationDate;

  @ManyToOne
  @JoinColumn(name = "member_id")
  private Member member;

  @Enumerated(EnumType.STRING)
  private LoanStatus status = LoanStatus.PENDING;

  @OneToOne(mappedBy = "loan")
  private LoanApproval approval;

  @OneToMany(mappedBy = "loan")
  private List<LoanRepayment> repayments = new ArrayList<>();
}
