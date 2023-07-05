package chamapool.domain.notifications.models;

import jakarta.persistence.*;
import java.time.Instant;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.CreatedDate;

@Entity
@Setter
@Getter
@Accessors(chain = true, fluent = true)
public class Notification {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private Integer id;

  @CreatedDate
  private Instant timestamp;

  private String title;
  private String message;
  private Integer relatedId;

  @Enumerated(EnumType.STRING)
  private NotificationType type;
}

