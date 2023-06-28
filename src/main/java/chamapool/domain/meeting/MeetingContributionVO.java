package chamapool.domain.meeting;

import chamapool.domain.meeting.models.MeetingContribution;

public record MeetingContributionVO(Integer memberId, Double amount, String memberName) {
  public MeetingContributionVO(MeetingContribution contribution) {
    this(contribution.member().id(), contribution.amount(), contribution.member().fullName());
  }
}
