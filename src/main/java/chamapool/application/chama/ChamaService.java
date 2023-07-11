package chamapool.application.chama;

import chamapool.application.transactions.TransactionsService;
import chamapool.domain.chama.Chama;
import chamapool.domain.chama.ChamaRepository;
import chamapool.domain.chama.ChamaVO;
import chamapool.domain.loans.Loan;
import chamapool.domain.loans.enums.LoanStatus;
import chamapool.domain.loans.repositories.LoanApplicationRepository;
import chamapool.domain.loans.repositories.LoanRepository;
import chamapool.domain.meeting.repositories.MeetingContributionRepository;
import chamapool.domain.meeting.repositories.MeetingRepository;
import chamapool.domain.member.repositories.InvitedMemberRepository;
import chamapool.domain.member.repositories.MemberRepository;
import chamapool.domain.transaction.TransactionRepository;
import chamapool.domain.transaction.TransactionType;
import java.util.HashMap;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChamaService {
  private final ChamaRepository chamaRepository;
  private final InvitedMemberRepository invitedMemberRepository;
  private final MemberRepository memberRepository;
  private final LoanRepository loanRepository;
  private final LoanApplicationRepository loanApplicationRepository;
  private final TransactionRepository transactionRepository;
  private final MeetingRepository meetingRepository;
  private final MeetingContributionRepository meetingContributionRepository;
  private final TransactionsService transactionsService;
  private final AccountService accountService;

  public ChamaVO getChamaDetails() {
    return this.chamaRepository
        .getChama()
        .map(ChamaVO::new)
        .orElseThrow(() -> new NoSuchElementException("Chama not found"));
  }

  public HashMap<String, Integer> getMemberSummary() {
    var members = (int) this.memberRepository.count();
    var invitedMembers = (int) this.invitedMemberRepository.count();
    var res = new HashMap<String, Integer>();
    res.put("members", members);
    res.put("invitedMembers", invitedMembers);
    return res;
  }

  public HashMap<String, Object> getLoanSummary() {
    var issuedLoans = (int) this.loanRepository.count();
    var loanApplications = (int) this.loanApplicationRepository.count();

    var totalAmountBorrowed = this.loanRepository.sumAmountBorrowed();
    var totalAmountRepaid = this.loanRepository.sumAmountPaid();

    var outstandingBalance =
        this.loanRepository.findAll().stream()
            .filter(loan -> loan.status() != LoanStatus.AWAITING_DISBURSEMENT)
            .mapToDouble(Loan::balance)
            .sum();

    var activeLoans = this.loanRepository.countByStatus(LoanStatus.ACTIVE);
    var overdueLoans = this.loanRepository.countByStatus(LoanStatus.OVERDUE);
    var repaidLoans = this.loanRepository.countByStatus(LoanStatus.REPAID);
    var pendingLoans = this.loanRepository.countByStatus(LoanStatus.AWAITING_DISBURSEMENT);

    var res = new HashMap<String, Object>();
    res.put("issuedLoans", issuedLoans);
    res.put("totalAmountBorrowed", totalAmountBorrowed);
    res.put("totalAmountRepaid", totalAmountRepaid);
    res.put("outstandingBalance", outstandingBalance);
    res.put("loanApplications", loanApplications);
    res.put("activeLoans", activeLoans);
    res.put("overdueLoans", overdueLoans);
    res.put("repaidLoans", repaidLoans);
    res.put("pendingLoans", pendingLoans);

    return res;
  }

  public void disburseDividends(DisburseDividendRequest request) {
    var membersCount = this.memberRepository.count();
    var members = this.memberRepository.findAll();

    var amountPerMember = request.amount() / membersCount;

    for (var member : members) {
      this.transactionsService.createTransaction(
          TransactionType.DIVIDEND,
          amountPerMember,
          String.format("Dividend disbursement to %s", member.fullName()));
    }
  }

  public void recordIncome(RecordIncomeRequest request) {
    for (var income : request.incomes()) {
      this.transactionsService.createTransaction(
          TransactionType.INVESTMENT_INCOME, income.amount(), income.description());
    }
  }

  public void recordWithdrawal(RecordWithdrawalRequest request) {
    for (var withdrawal : request.withdrawals()) {
      this.transactionsService.createTransaction(
          TransactionType.WITHDRAWAL, withdrawal.amount(), withdrawal.description());
    }
  }

  public HashMap<String, Double> getAccountSummary() {
    var balance = this.chamaRepository.getChama().stream().mapToDouble(Chama::accountBalance).sum();

    var transactions = this.transactionRepository.findAll();

    var res = new HashMap<String, Double>();
    res.put("accountBalance", balance);
    res.put("totalExpenses", this.accountService.getIncomeRevenue(transactions));
    res.put("totalIncome", this.accountService.getExpensesRevenue(transactions));

    return res;
  }

  public HashMap<String, Object> getMeetingsSummary() {
    var totalMeetingsHeld = (int) this.meetingRepository.count();
    var scheduledMeetings = this.meetingRepository.countByInitiatedIsFalse();

    var totalContributions = this.meetingContributionRepository.getTotalContributionsSum();

    var res = new HashMap<String, Object>();
    res.put("meetings", totalMeetingsHeld);
    res.put("scheduledMeetings", scheduledMeetings);
    res.put("totalContributions", totalContributions);
    return res;
  }
}
