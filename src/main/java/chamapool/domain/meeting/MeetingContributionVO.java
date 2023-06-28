package chamapool.domain.meeting;


//  {
//          memberId: 3,
//          amount: 1500,
//          memberName: " Lewis Ndungu",
//          },
public record MeetingContributionVO(
        Integer memberId,
        Integer amount,
        String memberName
) {
    public MeetingContributionVO(Meet) {
    }
}
