package chamapool.application.members.controllers;

import chamapool.application.members.MembersService;
import chamapool.application.members.responses.MultipleMemberResponse;
import chamapool.domain.member.VOs.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {
  private final MembersService membersService;

  @GetMapping()
  public MultipleMemberResponse retrieveMembers() {
    var members = this.membersService.retrieveMembers();
    return new MultipleMemberResponse(members);
  }

  @GetMapping("/{username}/profile")
  public MemberProfileVO retrieveMemberProfile(@PathVariable String username) {
    return this.membersService.retrieveMemberProfile(username);
  }

  @GetMapping("/{username}/kin")
  public NextOfKinVO retrieveMemberKin(@PathVariable String username) {
    return this.membersService.retrieveMemberNextOfKin(username);
  }

  @GetMapping("/{username}/occupation")
  public OccupationVO retrieveMemberOccupation(@PathVariable String username) {
    return this.membersService.retrieveMemberOccupation(username);
  }

  @GetMapping("/{username}/address")
  public AddressVO retrieveMemberAddress(@PathVariable String username) {
    return this.membersService.retrieveMemberAddress(username);
  }
}
