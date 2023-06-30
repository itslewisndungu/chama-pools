package chamapool.application.meetings;

import chamapool.application.meetings.requests.CreateMeetingRequest;
import chamapool.application.meetings.requests.MeetingAttendanceRequest;
import chamapool.application.meetings.requests.MeetingContributionsRequest;
import chamapool.domain.meeting.MeetingAttendanceVO;
import chamapool.domain.meeting.MeetingContributionVO;
import chamapool.domain.meeting.MeetingVO;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/meetings")
@RequiredArgsConstructor
public class MeetingsController {
  private final MeetingsService meetingsService;

  @GetMapping
  public List<MeetingVO> getAllMeetings() {
    return meetingsService.getAllMeetings();
  }

  @PostMapping("/schedule")
  @ResponseStatus(HttpStatus.CREATED)
  public MeetingVO createMeeting(@RequestBody CreateMeetingRequest request) {
    return meetingsService.createMeeting(request);
  }

  @GetMapping("/{meetingId}")
  public MeetingVO getMeetingById(@PathVariable Integer meetingId) {
    return meetingsService.getMeetingById(meetingId);
  }

  @GetMapping("/{meetingId}/attendance")
  public List<MeetingAttendanceVO> getMeetingAttendanceByMeetingId(
      @PathVariable Integer meetingId) {
    return this.meetingsService.getMeetingAttendance(meetingId);
  }

  @PostMapping("/{meetingId}/attendance")
  @ResponseStatus(HttpStatus.CREATED)
  public List<MeetingAttendanceVO> registerMeetingAttendance(
      @PathVariable Integer meetingId, @RequestBody MeetingAttendanceRequest request) {
    return meetingsService.registerMeetingAttendance(meetingId, request);
  }

  @GetMapping("/{meetingId}/contributions")
  public List<MeetingContributionVO> getMeetingContributionsByMeetingId(
      @PathVariable Integer meetingId) {
    return this.meetingsService.getMeetingContributions(meetingId);
  }

  @PostMapping("/{meetingId}/contributions")
  @ResponseStatus(HttpStatus.CREATED)
  public List<MeetingContributionVO> registerMeetingContributions(
      @PathVariable Integer meetingId, @RequestBody MeetingContributionsRequest request) {
    return meetingsService.registerMeetingContributions(meetingId, request);
  }

  @PostMapping("/{meetingId}/initiate")
  @ResponseStatus(HttpStatus.CREATED)
  public MeetingVO initiateMeeting(@PathVariable Integer meetingId) {
    return meetingsService.initiateMeeting(meetingId);
  }
}
