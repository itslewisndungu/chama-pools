package chamapool.domain.loans.repositories;

import chamapool.domain.loans.Loan;
import chamapool.domain.loans.LoanInstallment;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoanInstallmentsRepository extends JpaRepository<LoanInstallment, Integer> {
    List<LoanInstallment> findLoanInstallmentsByLoan(Loan loan);
}
