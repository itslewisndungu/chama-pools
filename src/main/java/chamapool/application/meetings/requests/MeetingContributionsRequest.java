package chamapool.application.meetings.requests;

import java.util.List;

public record MeetingContributionsRequest(
         List<Contribution> contributions
) {}
