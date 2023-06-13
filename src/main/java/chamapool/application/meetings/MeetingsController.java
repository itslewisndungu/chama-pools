package chamapool.application.meetings;

import chamapool.application.meetings.requests.CreateMeetingRequest;
import chamapool.application.meetings.requests.MeetingAttendanceRequest;
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

  // TODO: Use pageable to limit the number of meetings returned
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

  @PostMapping("/{id}/attendance")
  @ResponseStatus(HttpStatus.CREATED)
  public MeetingVO registerMeetingAttendance(
      @PathVariable Integer id, @RequestBody MeetingAttendanceRequest request) {
    return meetingsService.registerMeetingAttendance(id, request);
  }
}
