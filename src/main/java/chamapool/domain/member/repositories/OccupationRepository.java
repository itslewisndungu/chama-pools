package chamapool.domain.member.repositories;

import chamapool.domain.member.models.Member;
import chamapool.domain.member.models.Occupation;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OccupationRepository extends JpaRepository<Occupation, Integer> {
    Optional<Occupation> getOccupationByMember(Member member);
}
