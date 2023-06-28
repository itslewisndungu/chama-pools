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

  @PostMapping("/create")
  @ResponseStatus(HttpStatus.CREATED)
  public MeetingVO createMeeting(@RequestBody CreateMeetingRequest request) {
    return meetingsService.createMeeting(request);
  }

  @GetMapping("/{id}")
  public MeetingVO getMeetingById(@PathVariable Integer id) {
    return meetingsService.getMeetingById(id);
  }

  @GetMapping("/{id}/attendance")
  public List<MeetingAttendanceVO> getMeetingAttendanceById(@PathVariable Integer id) {
    return this.meetingsService.getMeetingAttendance(id);
  }

  @PostMapping("/{id}/attendance")
  @ResponseStatus(HttpStatus.CREATED)
  public List<MeetingAttendanceVO> registerMeetingAttendance(
      @PathVariable Integer id, @RequestBody MeetingAttendanceRequest request) {
    return meetingsService.registerMeetingAttendance(id, request);
  }

  @PostMapping("/{id}/contributions")
  @ResponseStatus(HttpStatus.CREATED)
  public List<MeetingContributionVO> registerMeetingContributions(
      @PathVariable Integer id, @RequestBody MeetingContributionsRequest request) {
    return meetingsService.registerMeetingContributions(id, request);
  }
}
