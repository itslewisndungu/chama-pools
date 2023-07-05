package chamapool.application.meetings;

import chamapool.application.meetings.requests.CreateMeetingRequest;
import chamapool.application.meetings.requests.MeetingAttendanceRequest;
import chamapool.application.meetings.requests.MeetingContributionsRequest;
import chamapool.application.notifications.NotificationsService;
import chamapool.application.transactions.TransactionsService;
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
import chamapool.domain.notifications.models.Notification;
import chamapool.domain.notifications.models.NotificationType;
import chamapool.domain.transaction.TransactionType;
import jakarta.transaction.Transactional;
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
  private final NotificationsService notificationsService;
  private final TransactionsService transactionsService;

  @Transactional
  public MeetingVO createMeeting(CreateMeetingRequest request) {
    Meeting meeting =
        new Meeting()
            .meetingDate(request.date())
            .title(request.title())
            .agenda(request.agenda())
            .category(request.category().orElse(MeetingCategory.MONTHLY_MEETING));

    meetingRepository.save(meeting);

    Notification notification =
        new Notification()
            .title("New Meeting")
            .message("A new meeting has been scheduled for " + meeting.meetingDate())
            .type(NotificationType.ANNOUNCEMENT)
            .relatedId(meeting.meetingId());
    this.notificationsService.sendGroupNotification(notification);

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
          this.meetingAttendanceRepository
              .getMeetingAttendanceByMemberAndMeeting(member, meeting)
              .orElse(new MeetingAttendance().member(member).meeting(meeting))
              .isPresent(a.isPresent())
              .apology(a.isPresent() ? null : "No apology");

      meetingAttendance.add(attendance);
    }

    meetingAttendanceRepository.saveAll(meetingAttendance);
    return meetingAttendance.stream().map(MeetingAttendanceVO::new).toList();
  }

  @Transactional
  public List<MeetingContributionVO> registerMeetingContributions(
      Integer id, MeetingContributionsRequest request) {
    Meeting meeting = meetingRepository.getReferenceById(id);
    List<MeetingContribution> contributions = new ArrayList<>();

    for (var c : request.contributions()) {
      Member member = memberRepository.getReferenceById(c.memberId());

      var contribution =
          this.contributionRepository
              .getMeetingContributionByMemberAndMeeting(member, meeting)
              .orElse(new MeetingContribution().member(member).meeting(meeting));

      contribution.amount(
          contribution.amount() == null ? c.amount() : contribution.amount() + c.amount());

      this.contributionRepository.save(contribution);
      this.transactionsService.createTransaction(TransactionType.CONTRIBUTION, c.amount());

      contributions.add(contribution);
    }

    // TODO: Fix bug
    return contributions.stream().map(MeetingContributionVO::new).toList();
  }

  @Transactional
  public MeetingVO initiateMeeting(Integer meetingId) {
    Meeting meeting = meetingRepository.getReferenceById(meetingId);
    meeting.initiated(true);
    meetingRepository.save(meeting);
    this.generateMemberMeetingAttendanceAndContributions(meeting);

    return new MeetingVO(meeting);
  }

  private void generateMemberMeetingAttendanceAndContributions(Meeting meeting) {
    var members = this.memberRepository.findAll();
    var attendances = new ArrayList<MeetingAttendance>();
    var contributions = new ArrayList<MeetingContribution>();

    for (var m : members) {
      var attendance = new MeetingAttendance().member(m).meeting(meeting).isPresent(false);
      attendances.add(attendance);

      var contribution = new MeetingContribution().member(m).meeting(meeting).amount(0.0);
      contributions.add(contribution);
    }

    this.meetingAttendanceRepository.saveAll(attendances);
    this.contributionRepository.saveAll(contributions);
  }
}
