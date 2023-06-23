package chamapool.domain.loans;

import chamapool.domain.loans.enums.LoanStatus;
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

  @CreatedDate private
  LocalDate approvedDate;

  private LocalDate startDate;
  private LocalDate expectedEndDate = LocalDate.now().plusMonths(3);
  private LocalDate endDate;

  @ManyToOne
  @JoinColumn(name = "member_id")
  private Member member;

  @Enumerated(EnumType.STRING)
  private LoanStatus status = LoanStatus.PENDING;

  @OneToMany(mappedBy = "loan")
  private List<LoanRepayment> repayments = new ArrayList<>();

  public Double interestRate() {
    return 10.0;
  }

  public Double interestEarned() {
    return amount * interestRate() / 100;
  }

  public Double amountPayable() {
    return this.amount + this.interestEarned();
  }
}
