package chamapool.application.meetings.requests;

import java.util.List;

public record MeetingAttendanceRequest(
        List<Attendance> attendances
) {}

