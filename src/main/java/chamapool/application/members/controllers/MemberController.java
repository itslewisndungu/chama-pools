package chamapool.application.members.controllers;

import chamapool.application.members.MembersService;
import chamapool.application.members.responses.MemberProfileResponse;
import chamapool.application.members.responses.MemberResponse;
import chamapool.application.members.responses.MultipleMemberResponse;
import chamapool.domain.member.VOs.MemberVO;
import chamapool.domain.member.models.Member;
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

  @GetMapping("/{username}")
  public MemberProfileResponse retrieveMemberProfile(@PathVariable String username) {
    var member = this.membersService.retrieveMemberProfile(username);
    return new MemberProfileResponse(member);
  }
}
