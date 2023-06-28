package chamapool.domain.meeting;

import chamapool.domain.meeting.enums.MeetingCategory;
import chamapool.domain.meeting.models.Meeting;
import chamapool.domain.meeting.models.MeetingAttendance;
import java.time.LocalDate;

public record MeetingVO(
        Integer id, LocalDate date, MeetingCategory kind, String agenda, Integer membersPresent) {
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
