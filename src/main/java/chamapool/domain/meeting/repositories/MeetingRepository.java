package chamapool.domain.meeting.repositories;

import chamapool.domain.meeting.models.Meeting;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MeetingRepository extends JpaRepository<Meeting, Integer> {
    Integer countByInitiatedIsFalse();

}
