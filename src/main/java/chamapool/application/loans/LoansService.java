package chamapool.application.loans;

import chamapool.application.loans.requests.LoanApplicationRequest;
import chamapool.domain.loans.Loan;
import chamapool.domain.loans.LoanApproval;
import chamapool.domain.loans.LoanApprovalRepository;
import chamapool.domain.loans.LoanRepository;
import chamapool.domain.member.models.Member;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoansService {
  private final LoanRepository loanRepository;
  private final LoanApprovalRepository loanApprovalRepository;

  @Transactional
  public void applyForLoan(Member member, LoanApplicationRequest req) {
    log.info("Member {} applying for loan: {}", member.firstName() + ' ' + member.lastName(), req);

    Loan loan = new Loan().member(member).amount(req.amount()).reasonForLoan(req.reasonForLoan());
    loanRepository.save(loan);

    LoanApproval approval = new LoanApproval().loan(loan);
    loanApprovalRepository.save(approval);

  }
}
