package chamapool.domain.loans.repositories;

import chamapool.domain.loans.LoanInstallment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoanRepaymentRepository extends JpaRepository<LoanInstallment, Integer> {}
