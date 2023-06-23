package chamapool.domain.loans.repositories;

import chamapool.domain.loans.LoanApplication;
import chamapool.domain.loans.LoanApproval;
import chamapool.domain.member.models.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LoanApprovalRepository extends JpaRepository<LoanApproval, Integer> {
  Optional<LoanApproval> getLoanApprovalsByStakeholderAndLoanApplication(
      Member stakeholder, LoanApplication loanApplication);
}
