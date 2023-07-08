package chamapool.application.members.services;

import chamapool.domain.loans.Loan;
import chamapool.domain.loans.enums.LoanApprovalStatus;
import chamapool.domain.loans.enums.LoanStatus;
import chamapool.domain.loans.repositories.LoanApplicationRepository;
import chamapool.domain.loans.repositories.LoanRepository;
import chamapool.domain.meeting.models.MeetingContribution;
import chamapool.domain.meeting.repositories.MeetingAttendanceRepository;
import chamapool.domain.meeting.repositories.MeetingContributionRepository;
import chamapool.domain.meeting.repositories.MeetingRepository;
import chamapool.domain.member.models.Member;
import java.util.HashMap;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberDashboardService {
  private final MeetingRepository meetingRepository;
  private final MeetingAttendanceRepository attendanceRepository;
  private final MeetingContributionRepository contributionRepository;
  private final LoanApplicationRepository loanApplicationRepository;
  private final LoanRepository loanRepository;

  public HashMap<String, Object> getMemberMeetingsSummary(Member member) {
    var meetingsAttended = this.attendanceRepository.countByMemberAndPresentIsTrue(member);

    var totalContributions =
        this.contributionRepository.getMeetingContributionsByMember(member).stream()
            .mapToDouble(MeetingContribution::amount)
            .sum();

    var scheduledMeetings = this.meetingRepository.countByInitiatedIsFalse();

    var res = new HashMap<String, Object>();

    res.put("meetingsAttended", meetingsAttended);
    res.put("totalContributions", totalContributions);
    res.put("scheduledMeeting", scheduledMeetings);

    return res;
  }

  public HashMap<String, Object> getMemberLoansSummary(Member member) {
    var borrowedLoans = this.loanRepository.countByMember(member);
    var loansBorrowed = this.loanRepository.getLoansByMember(member);

    var totalAmountBorrowed = loansBorrowed.stream().mapToDouble(Loan::amount).sum();

    var totalAmountRepaid = loansBorrowed.stream().mapToDouble(Loan::amountPaid).sum();
    var activeLoans = this.loanRepository.countByStatusAndMember(LoanStatus.ACTIVE, member);
    var overdueLoans = this.loanRepository.countByStatusAndMember(LoanStatus.OVERDUE, member);
    var repaidLoans = this.loanRepository.countByStatusAndMember(LoanStatus.REPAID, member);
    var pendingLoans =
        this.loanRepository.countByStatusAndMember(LoanStatus.AWAITING_DISBURSEMENT, member);

    var loanApplications =
        this.loanApplicationRepository.countByMemberAndApprovalStatus(
            member, LoanApprovalStatus.AWAITING_APPROVAL);

    var res = new HashMap<String, Object>();
    res.put("borrowedLoans", borrowedLoans);
    res.put("totalAmountBorrowed", totalAmountBorrowed);
    res.put("totalAmountRepaid", totalAmountRepaid);
    res.put("activeLoans", activeLoans);
    res.put("overdueLoans", overdueLoans);
    res.put("repaidLoans", repaidLoans);
    res.put("pendingLoans", pendingLoans);
    res.put("loanApplications", loanApplications);

    return res;
  }
}
