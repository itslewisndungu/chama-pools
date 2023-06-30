package chamapool.domain.meeting.models;

import chamapool.domain.meeting.enums.MeetingCategory;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
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

  private boolean initiated = false;

  @Enumerated(EnumType.STRING)
  private MeetingCategory category;

  @OneToMany(mappedBy = "meeting")
  private Set<MeetingAttendance> attendances = new HashSet<>();

  @OneToMany(mappedBy = "meeting")
  private Set<MeetingContribution> contributions = new HashSet<>();
}
