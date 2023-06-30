package chamapool.domain.member.models;

import jakarta.persistence.*;
import java.time.Instant;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

enum MembershipFeeStatus {
  PAID,
  UNPAID,
  PARTIALLY_PAID,
}

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

  private Double feeAmount;
  private Double amountPaid;

  private String paymentDate;

  public MembershipFeeStatus status() {
    if (amountPaid == 0) {
      return MembershipFeeStatus.UNPAID;
    } else if (amountPaid < feeAmount) {
      return MembershipFeeStatus.PARTIALLY_PAID;
    } else {
      return MembershipFeeStatus.PAID;
    }
  }
}
