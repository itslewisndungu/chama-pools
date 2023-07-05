package chamapool.domain.notifications.repositories;

import chamapool.domain.member.models.Member;
import chamapool.domain.notifications.models.MemberNotification;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MemberNotificationRepository extends JpaRepository<MemberNotification, Integer> {
  @Query("SELECT mn FROM MemberNotification mn WHERE mn.recipient = :recipient AND (mn.isRead = NOT(:isRead) OR :isRead IS NULL)")
  List<MemberNotification> getMemberNotifications(
          @Param("recipient") Member recipient,
          @Param("isRead") Boolean isRead);
}
