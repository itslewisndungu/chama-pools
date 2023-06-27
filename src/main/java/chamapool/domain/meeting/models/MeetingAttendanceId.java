package chamapool.domain.meeting.models;

import jakarta.persistence.Embeddable;
import java.io.Serial;
import java.io.Serializable;
import lombok.Data;

@Data
@Embeddable
public class MeetingAttendanceId implements Serializable {
  @Serial
  private static final long serialVersionUID = -5901385261909519167L;

  private Integer meetingId;
  private Integer memberId;
}
