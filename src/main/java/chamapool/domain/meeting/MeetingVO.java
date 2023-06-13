package chamapool.domain.meeting;

import chamapool.domain.meeting.enums.MeetingKind;
import chamapool.domain.meeting.enums.MeetingPresence;
import chamapool.domain.meeting.models.Meeting;
import java.time.LocalDate;

public record MeetingVO(
    Integer id, LocalDate date, MeetingKind kind, String agenda, Integer attendanceCount) {
  public MeetingVO(Meeting meeting) {
    this(
        meeting.meetingId(),
        meeting.date(),
        meeting.kind(),
        meeting.agenda(),
        meeting.meetingAttendanceList().stream()
            .filter(
                meetingAttendance -> meetingAttendance.presence().equals(MeetingPresence.PRESENT))
            .toList()
            .size());
  }
}
