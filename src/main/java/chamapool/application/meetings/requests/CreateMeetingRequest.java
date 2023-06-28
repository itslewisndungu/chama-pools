package chamapool.application.meetings.requests;

import chamapool.domain.meeting.enums.MeetingCategory;
import java.time.LocalDate;
import java.util.Optional;

public record CreateMeetingRequest(
        LocalDate date, Optional<MeetingCategory> category, String agenda, String title) {}
