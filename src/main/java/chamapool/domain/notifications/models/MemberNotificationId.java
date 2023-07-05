package chamapool.domain.notifications.models;

import jakarta.persistence.Embeddable;
import java.io.Serial;
import java.io.Serializable;
import lombok.Data;

@Data
@Embeddable
public class MemberNotificationId implements Serializable {
    @Serial
    private static final long serialVersionUID = -5617614705002668329L;

    private Integer memberId;
    private Integer notificationId;
}
