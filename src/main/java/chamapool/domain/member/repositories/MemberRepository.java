package chamapool.domain.member.repositories;

import chamapool.domain.member.models.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Integer> {
    Optional<Member> getMemberByUsername(String username);
}
