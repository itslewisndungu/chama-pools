package chamapool.domain.meeting.models;

import chamapool.domain.meeting.enums.MeetingKind;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Setter
@Getter
@Accessors(chain = true, fluent = true)
@EntityListeners(AuditingEntityListener.class)
public class Meeting {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "meeting_id")
  private Integer meetingId;

  @Column(name = "date")
  private LocalDate date;

  @Enumerated(EnumType.STRING)
  private MeetingKind kind;

  @OneToMany(mappedBy = "meeting")
  private List<MeetingAttendance> meetingAttendanceList = new ArrayList<>();
}
