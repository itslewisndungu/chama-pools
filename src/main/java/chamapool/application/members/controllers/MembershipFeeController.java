package chamapool.application.members.controllers;

import chamapool.application.members.requests.PayBulkMembershipFeesRequest;
import chamapool.application.members.requests.PayMembershipFeeRequest;
import chamapool.application.members.services.MembersService;
import chamapool.domain.member.VOs.MembershipFeeVO;
import chamapool.domain.member.models.Member;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/members/membership-fees")
@RequiredArgsConstructor
public class MembershipFeeController {
  private final MembersService membersService;

  @GetMapping("/outstanding")
  public List<MembershipFeeVO> retrieveActiveFees() {
    return this.membersService.retrieveMembersWithOutstandingMembershipFees();
  }

  @GetMapping("/{memberId}")
  public MembershipFeeVO retrieveMemberMembershipFee(@PathVariable Integer memberId) {
    return this.membersService.retrieveMemberMembershipFee(memberId);
  }

  @GetMapping("/me")
  public MembershipFeeVO retrieveMyMembershipFee(Member member) {
    return this.membersService.retrieveMemberMembershipFee(member);
  }

  @PostMapping("/{memberId}/pay-installment")
  @ResponseStatus(HttpStatus.CREATED)
  public MembershipFeeVO payMembershipFee(
      @PathVariable Integer memberId, @RequestBody PayMembershipFeeRequest request) {
    return this.membersService.payMembershipFee(memberId, request);
  }

  @PostMapping("/pay-bulk-installments")
  @ResponseStatus(HttpStatus.CREATED)
  public void payBulkMembershipFees(@RequestBody PayBulkMembershipFeesRequest request) {
    this.membersService.payBulkMembershipFees(request);
  }
}
