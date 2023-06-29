package chamapool.application.meetings;

import chamapool.application.meetings.requests.CreateMeetingRequest;
import chamapool.application.meetings.requests.MeetingAttendanceRequest;
import chamapool.application.meetings.requests.MeetingContributionsRequest;
import chamapool.domain.meeting.MeetingAttendanceVO;
import chamapool.domain.meeting.MeetingContributionVO;
import chamapool.domain.meeting.MeetingVO;
import chamapool.domain.meeting.enums.MeetingCategory;
import chamapool.domain.meeting.models.Meeting;
import chamapool.domain.meeting.models.MeetingAttendance;
import chamapool.domain.meeting.models.MeetingContribution;
import chamapool.domain.meeting.repositories.MeetingAttendanceRepository;
import chamapool.domain.meeting.repositories.MeetingContributionRepository;
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
  private final MeetingContributionRepository contributionRepository;
  private final MemberRepository memberRepository;

  public MeetingVO createMeeting(CreateMeetingRequest request) {
    Meeting meeting =
        new Meeting()
            .meetingDate(request.date())
            .title(request.title())
            .agenda(request.agenda())
            .category(request.category().orElse(MeetingCategory.MONTHLY_MEETING));

    meetingRepository.save(meeting);
    return new MeetingVO(meeting);
  }

  public List<MeetingVO> getAllMeetings() {
    return this.meetingRepository.findAll().stream().map(MeetingVO::new).toList();
  }

  public MeetingVO getMeetingById(Integer id) {
    return new MeetingVO(this.meetingRepository.getReferenceById(id));
  }

  public List<MeetingAttendanceVO> getMeetingAttendance(Integer meetingId) {
    return this.meetingRepository.getReferenceById(meetingId).attendances().stream()
        .map(MeetingAttendanceVO::new)
        .toList();
  }

  public List<MeetingContributionVO> getMeetingContributions(Integer meetingId) {
    return this.meetingRepository.getReferenceById(meetingId).contributions().stream()
        .map(MeetingContributionVO::new)
        .toList();
  }

  public List<MeetingAttendanceVO> registerMeetingAttendance(
      Integer meetingId, MeetingAttendanceRequest request) {
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
    return meetingAttendance.stream().map(MeetingAttendanceVO::new).toList();
  }

  public List<MeetingContributionVO> registerMeetingContributions(
      Integer id, MeetingContributionsRequest request) {
    Meeting meeting = meetingRepository.getReferenceById(id);
    List<MeetingContribution> contributions = new ArrayList<>();

    for (var c : request.contributions()) {
      Member member = memberRepository.getReferenceById(c.memberId());
      var contribution =
          new MeetingContribution().member(member).meeting(meeting).amount(c.amount());

      contributions.add(contribution);
    }

    this.contributionRepository.saveAll(contributions);

    return contributions.stream().map(MeetingContributionVO::new).toList();
  }
}
