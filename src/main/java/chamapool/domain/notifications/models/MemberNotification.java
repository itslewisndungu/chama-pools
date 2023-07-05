package chamapool.domain.notifications.models;

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
public class MemberNotification {
  @EmbeddedId private MemberNotificationId id = new MemberNotificationId();

  private Boolean read = false;

  @ManyToOne
  @MapsId("notificationId")
  @JoinColumn(name = "notification_id")
  private Notification notification;

  @ManyToOne
  @MapsId("memberId")
  @JoinColumn(name = "member_id")
  private Member recipient;
}
