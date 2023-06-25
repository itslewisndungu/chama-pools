package chamapool.domain.loans.repositories;

import chamapool.domain.loans.Loan;
import chamapool.domain.member.models.Member;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoanRepository extends JpaRepository<Loan, Integer> {
    List<Loan> getLoansByMember(Member member);
}
