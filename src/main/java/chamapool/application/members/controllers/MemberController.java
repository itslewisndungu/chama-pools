package chamapool.application.members.controllers;

import chamapool.application.loans.LoansService;
import chamapool.application.loans.responses.LoanEligibilityResponse;
import chamapool.application.members.MembersService;
import chamapool.application.members.responses.MemberProfileResponse;
import chamapool.application.members.responses.MultipleMemberResponse;
import chamapool.domain.member.models.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {
  private final MembersService membersService;
  private final LoansService loansService;

  @GetMapping()
  public MultipleMemberResponse retrieveMembers() {
    var members = this.membersService.retrieveMembers();
    return new MultipleMemberResponse(members);
  }

  @GetMapping("/{username}")
  public MemberProfileResponse retrieveMemberProfile(@PathVariable String username) {
    var member = this.membersService.retrieveMemberProfile(username);
    return new MemberProfileResponse(member);
  }

  @GetMapping("/loan-eligibility")
  public LoanEligibilityResponse checkLoanEligibility(Member member) {
    return this.loansService.checkLoanEligibility(member);
  }
}
