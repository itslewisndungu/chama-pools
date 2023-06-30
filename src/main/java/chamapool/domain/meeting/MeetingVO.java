package chamapool.domain.meeting;

import chamapool.domain.meeting.enums.MeetingCategory;
import chamapool.domain.meeting.models.Meeting;
import chamapool.domain.meeting.models.MeetingAttendance;
import java.time.LocalDate;

public record MeetingVO(
    Integer id,
    LocalDate date,
    String title,
    MeetingCategory category,
    String agenda,
    Integer membersPresent,
    Boolean initiated) {
  public MeetingVO(Meeting meeting) {
    this(
        meeting.meetingId(),
        meeting.meetingDate(),
        meeting.title(),
        meeting.category(),
        meeting.agenda(),
        meeting.attendances().stream().filter(MeetingAttendance::isPresent).toList().size(),
        meeting.initiated());
  }
}
