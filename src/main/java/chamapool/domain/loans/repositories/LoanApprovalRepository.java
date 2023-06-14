package chamapool.domain.loans.repositories;

import chamapool.domain.loans.LoanApproval;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoanApprovalRepository extends JpaRepository<LoanApproval, Integer> {}
