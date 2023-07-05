package chamapool.application.notifications;

import chamapool.domain.member.models.Member;
import chamapool.domain.notifications.NotificationVO;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationsController {
    private final NotificationsService notificationsService;

    @GetMapping("/member")
    public List<NotificationVO> getMemberNotifications(Member member) {
        return this.notificationsService.getMemberNotifications(member);
    }
}
