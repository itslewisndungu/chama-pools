package chamapool.domain.member.repositories;

import chamapool.domain.member.enums.MemberRole;
import chamapool.domain.member.models.Member;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MemberRepository extends JpaRepository<Member, Integer> {
  Optional<Member> getMemberByUsername(String username);

  @Query("SELECT m FROM Member m WHERE ?1 in (SELECT r.name FROM m.roles r) ")
  List<Member> getMembersByRole(MemberRole role);
}
