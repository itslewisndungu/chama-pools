package chamapool.application.meetings;

import chamapool.application.meetings.requests.CreateMeetingRequest;
import chamapool.application.meetings.requests.MeetingAttendanceRequest;
import chamapool.domain.meeting.MeetingVO;
import chamapool.domain.meeting.enums.MeetingKind;
import chamapool.domain.meeting.enums.MeetingPresence;
import chamapool.domain.meeting.models.Meeting;
import chamapool.domain.meeting.models.MeetingAttendance;
import chamapool.domain.meeting.repositories.MeetingAttendanceRepository;
import chamapool.domain.meeting.repositories.MeetingRepository;
import chamapool.domain.member.repositories.MemberRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MeetingsService {
  private final MeetingRepository meetingRepository;
  private final MeetingAttendanceRepository meetingAttendanceRepository;
  private final MemberRepository memberRepository;

  public MeetingVO createMeeting(CreateMeetingRequest request) {
    Meeting meeting =
        new Meeting()
            .date(request.date())
            .agenda(request.agenda())
            .kind(request.kind().orElse(MeetingKind.REGULAR));

    meetingRepository.save(meeting);

    return new MeetingVO(meeting);
  }

  public MeetingVO registerMeetingAttendance(Integer meetingId, MeetingAttendanceRequest request) {
    var meeting = meetingRepository.getReferenceById(meetingId);

    var membersPresent =
        request.membersPresent().stream().map(memberRepository::getReferenceById).toList();

    membersPresent.forEach(
        member ->
            meetingAttendanceRepository.save(
                new MeetingAttendance()
                    .meeting(meeting)
                    .member(member)
                    .presence(MeetingPresence.PRESENT)));

    var membersAbsentWithApology =
        request.membersAbsentWithApology().stream()
            .map(memberRepository::getReferenceById)
            .toList();

    membersAbsentWithApology.forEach(
        member ->
            meetingAttendanceRepository.save(
                new MeetingAttendance()
                    .meeting(meeting)
                    .member(member)
                    .presence(MeetingPresence.ABSENT_WITH_APOLOGY)));

    var membersAbsentWithoutApology =
        request.membersAbsentWithoutApology().stream()
            .map(memberRepository::getReferenceById)
            .toList();

    membersAbsentWithoutApology.forEach(
        member ->
            meetingAttendanceRepository.save(
                new MeetingAttendance()
                    .meeting(meeting)
                    .member(member)
                    .presence(MeetingPresence.ABSENT_WITHOUT_APOLOGY)));

    return new MeetingVO(meeting);
  }

  public List<MeetingVO> getAllMeetings() {
    return this.meetingRepository.findAll().stream().map(MeetingVO::new).toList();
  }

  public MeetingVO getMeetingById(Integer id) {
    return new MeetingVO(this.meetingRepository.getReferenceById(id));
  }
}
