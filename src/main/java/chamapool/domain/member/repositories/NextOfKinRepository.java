package chamapool.domain.member.repositories;

import chamapool.domain.member.models.Member;
import chamapool.domain.member.models.NextOfKin;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NextOfKinRepository extends JpaRepository<NextOfKin, Integer> {
    Optional<NextOfKin> getNextOfKinByMember(Member member);
}
