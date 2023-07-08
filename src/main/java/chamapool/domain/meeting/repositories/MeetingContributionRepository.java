package chamapool.domain.meeting.repositories;

import chamapool.domain.meeting.models.Meeting;
import chamapool.domain.meeting.models.MeetingContribution;
import chamapool.domain.member.models.Member;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MeetingContributionRepository extends JpaRepository<MeetingContribution, Integer> {
    Optional<MeetingContribution> getMeetingContributionByMemberAndMeeting(Member member, Meeting meeting);

    List<MeetingContribution> getMeetingContributionsByMember(Member member);
}
