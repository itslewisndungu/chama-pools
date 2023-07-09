package chamapool.domain.loans.repositories;

import chamapool.domain.loans.Loan;
import chamapool.domain.loans.enums.LoanStatus;
import chamapool.domain.member.models.Member;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface LoanRepository extends JpaRepository<Loan, Integer> {
  List<Loan> getLoansByMember(Member member);

  Integer countByStatus(LoanStatus status);
  Integer countByStatusAndMember(LoanStatus status, Member member);


  Integer countByMember(Member member);

  @Query("SELECT SUM(l.amountPaid) FROM Loan l")
  Double sumAmountPaid();

  @Query("SELECT SUM(l.amount) FROM Loan l")
  Double sumAmountBorrowed();
}
