package chamapool.application.meetings.requests;

import java.util.List;

public record MeetingAttendanceRequest(
    List<Integer> membersPresent,
    List<Integer> membersAbsentWithApology,
    List<Integer> membersAbsentWithoutApology) {}
