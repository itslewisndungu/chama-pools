package chamapool.domain.meeting.repositories;

import chamapool.domain.meeting.models.MeetingContribution;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MeetingContributionRepository extends JpaRepository<MeetingContribution, Integer> {}
