package chamapool.domain.member.models;

import jakarta.persistence.*;
import java.time.Instant;
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

  private Double feeAmount;

  @Enumerated(EnumType.STRING)
  private MembershipFeeStatus paymentStatus = MembershipFeeStatus.UNPAID;

  private String paymentDate;
}

enum MembershipFeeStatus {
  PAID,
  UNPAID
}
