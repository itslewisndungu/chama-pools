package chamapool.application.notifications;

import chamapool.domain.member.models.Member;
import chamapool.domain.member.repositories.MemberRepository;
import chamapool.domain.notifications.NotificationVO;
import chamapool.domain.notifications.models.MemberNotification;
import chamapool.domain.notifications.models.Notification;
import chamapool.domain.notifications.repositories.MemberNotificationRepository;
import chamapool.domain.notifications.repositories.NotificationRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationsService {
  private final MemberNotificationRepository memberNotificationRepository;
  private final MemberRepository memberRepository;
  private final NotificationRepository notificationRepository;

  public void sendNotification(Member member, Notification notification) {
    log.info(
        "Sending notification of type {} and message {} to member {}",
        notification.type(),
        notification.message(),
        member.id());

    var memberNotification = new MemberNotification().notification(notification).recipient(member);
    this.memberNotificationRepository.save(memberNotification);
    log.info("Notification sent");
  }

  public void sendGroupNotification(Notification notification) {
    log.info(
        "Sending group notification of type {} and message {}",
        notification.type(),
        notification.message());

    this.notificationRepository.save(notification);
    var members = this.memberRepository.findAll();
    for (Member member : members) {
      this.sendNotification(member, notification);
    }

    log.info("Group notification sent");
  }

  public List<NotificationVO> getMemberNotifications(Member member) {
    return this.memberNotificationRepository.findMemberNotificationsByRecipient(member)
            .stream().map(NotificationVO::new).toList();
  }
}
