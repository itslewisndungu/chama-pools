package chamapool.domain.meeting.models;

import chamapool.domain.meeting.enums.MeetingCategory;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
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
public class Meeting {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "meeting_id")
  private Integer meetingId;

  private String title;
  private String agenda;

  @CreatedDate private LocalDate dateOfScheduling;

  private LocalDate meetingDate;

  @Enumerated(EnumType.STRING)
  private MeetingCategory category;

  @OneToMany(mappedBy = "meeting")
  private List<MeetingAttendance> attendances = new ArrayList<>();

  @OneToMany(mappedBy = "meeting")
  private List<MeetingContribution> contributions = new ArrayList<>();
}
