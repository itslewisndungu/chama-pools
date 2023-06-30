package chamapool.domain.loans.repositories;

import chamapool.domain.loans.LoanRepayment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoanRepaymentRepository extends JpaRepository<LoanRepayment, Integer> {}
