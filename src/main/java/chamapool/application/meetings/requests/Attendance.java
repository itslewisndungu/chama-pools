package chamapool.application.meetings.requests;

import java.util.Optional;

public record Attendance(Integer memberId, Boolean isPresent, Optional<String> apology) {}
