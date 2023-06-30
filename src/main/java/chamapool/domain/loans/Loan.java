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
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private Integer id;

  private Double amount;
  private Double amountPaid = 0.0;
  private String reasonForLoan;

  @CreatedDate private LocalDate approvedDate;

  private LocalDate startDate;
  private LocalDate endDate;

  @ManyToOne
  @JoinColumn(name = "member_id")
  private Member member;

  @Enumerated(EnumType.STRING)
  private LoanStatus status = LoanStatus.AWAITING_DISBURSEMENT;

  @OneToMany(mappedBy = "loan")
  private List<LoanRepayment> repayments = new ArrayList<>();

  public Double interestRate() {
    return 10.0;
  }

  public LocalDate dueDate() {
    if (this.startDate != null) {
      return this.startDate.plusMonths(3);
    } else {
      return null;
    }
  }

  public Double interestEarned() {
    return amount * interestRate() / 100;
  }

  public Double amountPayable() {
    return this.amount + this.interestEarned();
  }
}
