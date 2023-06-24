package chamapool.domain.loans.repositories;

import chamapool.domain.loans.LoanApplication;
import chamapool.domain.loans.enums.LoanApprovalStatus;
import chamapool.domain.member.models.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoanApplicationRepository extends JpaRepository<LoanApplication, Integer> {
  Optional<LoanApplication> getLoanApplicationByMemberAndApprovalStatus(
      Member member, LoanApprovalStatus approvalStatus);
}
