package chamapool.domain.meeting.repositories;

import chamapool.domain.meeting.models.MeetingAttendance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MeetingAttendanceRepository extends JpaRepository<MeetingAttendance, Integer> {}
