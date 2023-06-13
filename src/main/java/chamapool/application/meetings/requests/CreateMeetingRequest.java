package chamapool.application.meetings.requests;

import chamapool.domain.meeting.enums.MeetingKind;
import java.time.LocalDate;
import java.util.Optional;

public record CreateMeetingRequest(LocalDate date, Optional<MeetingKind> kind, String agenda) {}
