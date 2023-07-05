package chamapool.application.notifications;

import chamapool.domain.notifications.NotificationVO;
import java.util.List;

public record MemberNotificationsResponse(List<NotificationVO> notifications, Integer unreadCount) {
  public MemberNotificationsResponse(List<NotificationVO> notifications) {
    this(notifications, (int) notifications.stream().filter(n -> !n.isRead()).count());
  }
}
