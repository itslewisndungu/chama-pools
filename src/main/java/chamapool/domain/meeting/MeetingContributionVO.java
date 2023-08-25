package chamapool.domain.meeting;

import chamapool.domain.meeting.models.MeetingContribution;
import chamapool.domain.meeting.repositories.IContributionsSum;
import lombok.Data;

@Data
public class MeetingContributionVO {
  private Integer memberId;
  private Double amount;
  private String memberName;

  public MeetingContributionVO(Integer memberId, Double amount, String memberName) {
    this.memberId = memberId;
    this.amount = amount;
    this.memberName = memberName;
  }

  public MeetingContributionVO(MeetingContribution contribution) {
    this(contribution.member().id(), contribution.amount(), contribution.member().fullName());
  }

  public MeetingContributionVO(IContributionsSum sum) {
    this(sum.getId(), sum.getTotalContributions(), sum.getFirstName() + " " + sum.getLastName());
  }
}
