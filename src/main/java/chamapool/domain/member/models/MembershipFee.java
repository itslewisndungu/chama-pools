package chamapool.domain.member.models;

import chamapool.domain.member.enums.MembershipFeeStatus;
import jakarta.persistence.*;
import java.time.Instant;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Setter
@Getter
@Accessors(chain = true, fluent = true)
@EntityListeners(AuditingEntityListener.class)
public class MembershipFee {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private Integer id;

  @OneToOne(mappedBy = "membershipFee")
  private Member member;

  @CreatedDate private Instant createdAt;

  private Double amount;
  private Double amountPaid;

  private LocalDate paymentDate;

  public Double balance() {
    return this.amount - this.amountPaid;
  }

  public MembershipFeeStatus status() {
    if (this.amountPaid == 0) {
      return MembershipFeeStatus.UNPAID;
    } else if (this.amountPaid < this.amount) {
      return MembershipFeeStatus.PARTIALLY_PAID;
    } else {
      return MembershipFeeStatus.PAID;
    }
  }
}
