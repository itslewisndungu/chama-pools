package chamapool.application.notifications;

import chamapool.domain.member.models.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationsController {
    private final NotificationsService notificationsService;

    @GetMapping("/member")
    public MemberNotificationsResponse getMemberNotifications(
            Member member,
            @RequestParam(value="unread", required = false) boolean unread
    ) {
        var notifications =  this.notificationsService.getMemberNotifications(member, unread);
        return new MemberNotificationsResponse(notifications);
    }
}
