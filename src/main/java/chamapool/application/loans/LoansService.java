package chamapool.application.loans;

import chamapool.domain.loans.VO.*;
import chamapool.domain.loans.repositories.LoanRepository;
import chamapool.domain.member.models.Member;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoansService {
  private final LoanRepository loanRepository;

  public LoanVO retrieveLoan(Integer loanId) {
    var loan = this.loanRepository.getReferenceById(loanId);
    return new LoanVO(loan);
  }

  public List<LoanVO> retrieveMemberLoans(Member member) {
    return this.loanRepository.getLoansByMember(member).stream().map(LoanVO::new).toList();
  }
}
