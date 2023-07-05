package chamapool.application.notifications;

import chamapool.domain.member.models.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationsController {
  private final NotificationsService notificationsService;

  @GetMapping("/member")
  public MemberNotificationsResponse getMemberNotifications(
      Member member, @RequestParam(value = "unread", required = false) boolean unread) {
    var notifications = this.notificationsService.getMemberNotifications(member, unread);
    return new MemberNotificationsResponse(notifications);
  }

  @PostMapping("/member/read-all")
  @ResponseStatus(HttpStatus.CREATED)
  public void markAllMemberNotificationsAsRead(Member member) {
    this.notificationsService.markAllAsRead(member);
  }
}
