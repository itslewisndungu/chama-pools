package chamapool.domain.meeting.repositories;

import chamapool.domain.meeting.models.Meeting;
import chamapool.domain.meeting.models.MeetingContribution;
import chamapool.domain.member.models.Member;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MeetingContributionRepository extends JpaRepository<MeetingContribution, Integer> {
    Optional<MeetingContribution> getMeetingContributionByMemberAndMeeting(Member member, Meeting meeting);

    List<MeetingContribution> getMeetingContributionsByMember(Member member);

  @Query(value = """
              select m.member.id as id,
              m.member.firstName as firstName,
              m.member.lastName as lastName,
              sum(m.amount) as totalContributions from MeetingContribution as m
              GROUP BY  m.member.id,  m.member.firstName, m.member.lastName
              order by totalContributions desc
          """)
  List<IContributionsSum> getMeetingContributionsGroupByMember();
}
