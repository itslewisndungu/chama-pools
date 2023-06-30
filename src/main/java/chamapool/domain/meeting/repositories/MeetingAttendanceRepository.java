package chamapool.domain.meeting.repositories;

import chamapool.domain.meeting.models.Meeting;
import chamapool.domain.meeting.models.MeetingAttendance;
import chamapool.domain.member.models.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MeetingAttendanceRepository extends JpaRepository<MeetingAttendance, Integer> {
    Optional<MeetingAttendance> getMeetingAttendanceByMemberAndMeeting(Member member, Meeting meeting);
}
