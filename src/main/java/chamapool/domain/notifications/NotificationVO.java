package chamapool.domain.notifications;

import chamapool.domain.notifications.models.MemberNotification;

public record NotificationVO(
    String title, String content, String type, Integer relatedId) {
  public NotificationVO(MemberNotification notification) {
    this(
        notification.notification().title(),
        notification.notification().message(),
        notification.notification().type().name(),
        notification.notification().relatedId());
  }
}