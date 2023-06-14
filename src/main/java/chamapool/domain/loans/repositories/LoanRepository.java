package chamapool.domain.loans.repositories;

import chamapool.domain.loans.Loan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoanRepository extends JpaRepository<Loan, Integer> {}
