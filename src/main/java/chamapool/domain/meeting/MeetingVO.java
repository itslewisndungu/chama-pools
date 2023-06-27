package chamapool.domain.meeting;

import chamapool.domain.meeting.enums.MeetingKind;
import chamapool.domain.meeting.models.Meeting;
import chamapool.domain.meeting.models.MeetingAttendance;
import java.time.LocalDate;

public record MeetingVO(
    Integer id, LocalDate date, MeetingKind kind, String agenda, Integer membersPresent) {
  public MeetingVO(Meeting meeting) {
    this(
        meeting.meetingId(),
        meeting.meetingDate(),
        meeting.kind(),
        meeting.agenda(),
        meeting.meetingAttendanceList().stream()
            .filter(MeetingAttendance::isPresent)
            .toList()
            .size());
  }
}
