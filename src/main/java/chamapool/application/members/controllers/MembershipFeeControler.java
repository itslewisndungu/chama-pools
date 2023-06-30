package chamapool.application.members.controllers;

import chamapool.application.members.MembersService;
import chamapool.application.members.requests.PayMembershipFeeRequest;
import chamapool.domain.member.VOs.MembershipFeeVO;
import chamapool.domain.member.models.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/membership-fees")
@RequiredArgsConstructor
public class MembershipFeeControler {
  private final MembersService membersService;

  @PostMapping("/{memberId}/pay")
  public MembershipFeeVO payMembershipFee(
      @PathVariable Integer memberId, @RequestBody PayMembershipFeeRequest request) {
    return this.membersService.payMembershipFee(memberId, request);
  }

  @GetMapping("/{memberId}")
  public MembershipFeeVO retrieveMemberMembershipFee(@PathVariable Integer memberId) {
    return this.membersService.retrieveMemberMembershipFee(memberId);
  }

  @GetMapping("/me")
  public MembershipFeeVO retrieveMyMembershipFee(Member member) {
    return this.membersService.retrieveMemberMembershipFee(member);
  }
}
