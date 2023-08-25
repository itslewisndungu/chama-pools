package chamapool.domain.notifications;

import java.time.Instant;

import chamapool.domain.notifications.models.MemberNotification;

public record NotificationVO(
        String title, String message, String type, Boolean read, Instant date, Integer relatedId) {
  public NotificationVO(MemberNotification notification) {
    this(
        notification.notification().title(),
        notification.notification().message(),
        notification.notification().type().name(),
        notification.read(),
        notification.notification().timestamp(),
        notification.notification().relatedId());
  }
}
