package chamapool.application.meetings;

import chamapool.application.meetings.requests.CreateMeetingRequest;
import chamapool.application.meetings.requests.MeetingAttendanceRequest;
import chamapool.domain.meeting.MeetingVO;
import chamapool.domain.meeting.enums.MeetingKind;
import chamapool.domain.meeting.models.Meeting;
import chamapool.domain.meeting.models.MeetingAttendance;
import chamapool.domain.meeting.repositories.MeetingAttendanceRepository;
import chamapool.domain.meeting.repositories.MeetingRepository;
import chamapool.domain.member.models.Member;
import chamapool.domain.member.repositories.MemberRepository;
import java.util.ArrayList;
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
            .meetingDate(request.date())
            .title(request.title())
            .agenda(request.agenda())
            .kind(request.kind().orElse(MeetingKind.MONTHLY_MEETING));

    meetingRepository.save(meeting);
    return new MeetingVO(meeting);
  }

  public MeetingVO registerMeetingAttendance(Integer meetingId, MeetingAttendanceRequest request) {
    Meeting meeting = meetingRepository.getReferenceById(meetingId);
    List<MeetingAttendance> meetingAttendance = new ArrayList<>();

    for (var a : request.attendances()) {
      Member member = memberRepository.getReferenceById(a.memberId());

      var attendance =
          new MeetingAttendance()
              .meeting(meeting)
              .member(member)
              .isPresent(a.isPresent())
              .apology(a.isPresent() ? null : "No apology");

      meetingAttendance.add(attendance);
    }

    meetingAttendanceRepository.saveAll(meetingAttendance);
    return new MeetingVO(meeting);
  }

  public List<MeetingVO> getAllMeetings() {
    return this.meetingRepository.findAll().stream().map(MeetingVO::new).toList();
  }

  public MeetingVO getMeetingById(Integer id) {
    return new MeetingVO(this.meetingRepository.getReferenceById(id));
  }
}
