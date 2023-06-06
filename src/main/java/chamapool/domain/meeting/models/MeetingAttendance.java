package chamapool.domain.meeting.models;

import chamapool.domain.meeting.enums.MeetingPresence;
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
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @ManyToOne
  @JoinColumn(name = "meeting_id")
  private Meeting meeting;

  @ManyToOne
  @JoinColumn(name = "member_id")
  private Member member;

  @Enumerated(EnumType.STRING)
  private MeetingPresence presence;
}
