package chamapool.domain.meeting.models;

import chamapool.domain.member.models.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Setter
@Getter
@Accessors(chain = true, fluent = true)
@EntityListeners(AuditingEntityListener.class)
public class MeetingAttendance {
  @EmbeddedId
  private MeetingAttendanceId id = new MeetingAttendanceId();

  @MapsId("meetingId")
  @ManyToOne
  @JoinColumn(name = "meeting_id")
  private Meeting meeting;

  @MapsId("memberId")
  @ManyToOne
  @JoinColumn(name = "member_id")
  private Member member;

  private boolean present;
  private String apology;
}
