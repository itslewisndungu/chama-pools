package chamapool.domain.meeting.models;

import jakarta.persistence.Embeddable;
import java.io.Serial;
import java.io.Serializable;
import lombok.Data;

@Data
@Embeddable
public class MeetingContributionId implements Serializable {
    @Serial
    private static final long serialVersionUID = -768245077499099648L;

    private Integer meetingId;
    private Integer memberId;
}
