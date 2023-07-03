package chamapool.domain.member.models;

import jakarta.persistence.*;
import java.time.Instant;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Setter
@Getter
@EntityListeners(AuditingEntityListener.class)
@Accessors(chain = true, fluent = true)
public class PasswordResetToken {

  // 2 hours
  private static final int EXPIRATION = 60 * 60 * 2;

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private Integer id;

  private String token;

  @OneToOne(targetEntity = Member.class, fetch = FetchType.EAGER)
  @JoinColumn(nullable = false, name = "user_id")
  private Member member;

  private Instant expiryDate = Instant.now().plusSeconds(EXPIRATION);
}
