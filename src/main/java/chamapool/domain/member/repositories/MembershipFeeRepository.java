package chamapool.domain.member.repositories;

import chamapool.domain.member.models.Member;
import chamapool.domain.member.models.MembershipFee;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MembershipFeeRepository extends JpaRepository<MembershipFee, Integer> {
  MembershipFee getByMember(Member member);

  @Query("SELECT mf FROM MembershipFee mf WHERE mf.amountPaid < mf.amount")
  List<MembershipFee> getOutstandingMembershipFees();
}
