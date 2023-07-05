package chamapool.domain.notifications.repositories;

import chamapool.domain.notifications.models.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Integer> {
}
