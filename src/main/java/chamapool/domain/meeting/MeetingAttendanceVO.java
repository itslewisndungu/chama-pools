package chamapool.domain.meeting;

import chamapool.domain.meeting.models.MeetingAttendance;

public record MeetingAttendanceVO(
    Integer memberId, String memberName, Boolean isPresent, String apology) {
    public MeetingAttendanceVO(MeetingAttendance attendance){
        this(attendance.member().id(), attendance.member().fullName(), attendance.present(), attendance.apology());
    }
}
