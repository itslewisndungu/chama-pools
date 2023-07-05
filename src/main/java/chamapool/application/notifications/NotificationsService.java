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

  public void sendGroupNotification(Notification notification) {
    log.info(
        "Sending group notification of type {} and message {}",
        notification.type(),
        notification.message());

    var members = this.memberRepository.findAll();
    for (Member member : members) {
      this.sendNotification(member, notification);
    }

    log.info("Group notification sent");
  }

  public void sendMemberNotification(Member recipient, Notification notification) {
    log.info(
        "Sending member notification of type '{}' and message '{}' to member {}",
        notification.type(),
        notification.message(),
        recipient.id());
    this.sendNotification(recipient, notification);
    log.info("Member notification sent");
  }

  public void sendAdminNotification(Notification notification) {
    log.info(
        "Sending admin notification of type {} and message {}",
        notification.type(),
        notification.message());

    var chairman = this.memberRepository.findChairman().orElseThrow();
    var secretary = this.memberRepository.findSecretary().orElseThrow();
    var treasurer = this.memberRepository.findTreasurer().orElseThrow();

    this.sendNotification(chairman, notification);
    this.sendNotification(secretary, notification);
    this.sendNotification(treasurer, notification);

    log.info("Admin notification sent");
  }

  public List<NotificationVO> getMemberNotifications(Member member, boolean unread) {
    var filterByUnread = unread ? Boolean.TRUE : null;

    return this.memberNotificationRepository.getMemberNotifications(member, filterByUnread).stream()
        .map(NotificationVO::new)
        .toList();
  }

  private void sendNotification(Member recipient, Notification notification) {
    this.notificationRepository.save(notification);

    var memberNotification =
        new MemberNotification().notification(notification).recipient(recipient);

    this.memberNotificationRepository.save(memberNotification);
  }

  public void markAllAsRead(Member member) {
    var unreadNotifications =
        this.memberNotificationRepository.getMemberNotifications(member, false);

    for (MemberNotification notification : unreadNotifications) {
      notification.read(true);
      this.memberNotificationRepository.save(notification);
    }
  }
}
