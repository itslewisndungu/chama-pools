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
  private Integer durationInMonths = 3;
  private Double interestRate;

  @Enumerated(EnumType.STRING)
  private LoanStatus status = LoanStatus.AWAITING_DISBURSEMENT;

  @ManyToOne
  @JoinColumn(name = "member_id")
  private Member member;

  @OneToMany(mappedBy = "loan")
  private List<LoanInstallment> repayments = new ArrayList<>();

  public Double interestEarned() {
    return amount * interestRate() / 100;
  }

  public Double amountPayable() {
    return this.amount + this.interestEarned();
  }

  public Double balance() {
    return this.amountPayable() - this.amountPaid();
  }

  public LocalDate dueDate() {
    return this.startDate.plusMonths(this.durationInMonths);
  }
}
