package chamapool.domain.notifications.repositories;

import chamapool.domain.member.models.Member;
import chamapool.domain.notifications.models.MemberNotification;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberNotificationRepository extends JpaRepository<MemberNotification, Integer> {
    List<MemberNotification> findMemberNotificationsByRecipient(Member recipient);
}
