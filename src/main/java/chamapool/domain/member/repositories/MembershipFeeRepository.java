package chamapool.domain.member.repositories;

import chamapool.domain.member.models.Member;
import chamapool.domain.member.models.MembershipFee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MembershipFeeRepository extends JpaRepository<MembershipFee, Integer> {
  MembershipFee getByMember(Member member);
}