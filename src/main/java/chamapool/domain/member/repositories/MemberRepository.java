package chamapool.domain.member.repositories;

import chamapool.domain.member.models.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MemberRepository extends JpaRepository<Member, Integer> {
  Optional<Member> getMemberByUsername(String username);

  @Query("SELECT m FROM Member m WHERE 'CHAIRMAN' in (SELECT r.name FROM m.roles r) ")
  Optional<Member> findChairman();

  @Query("SELECT m FROM Member m WHERE 'SECRETARY' in (SELECT r.name FROM m.roles r) ")
  Optional<Member> findSecretary();

  @Query("SELECT m FROM Member m WHERE 'TREASURER' in (SELECT r.name FROM m.roles r) ")
  Optional<Member> findTreasurer();
}
