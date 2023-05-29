package chamapool.application.members.controllers;

import chamapool.application.members.MembersService;
import chamapool.application.members.requests.AcceptInvitationRequest;
import chamapool.application.members.requests.NewMemberRequest;
import chamapool.application.members.responses.InviteMemberResponse;
import chamapool.application.members.responses.MemberResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/members/invite")
@RequiredArgsConstructor
public class MemberInvitationController {
  private final MembersService membersService;

  @PostMapping("/")
  @ResponseStatus(HttpStatus.CREATED)
  @PreAuthorize("hasAnyRole('CHAIRMAN', 'SECRETARY')")
  public InviteMemberResponse inviteNewMember(@RequestBody NewMemberRequest request) {
    var invitedMember = this.membersService.inviteMember(request);
    return new InviteMemberResponse(invitedMember);
  }

  @PostMapping("/{inviteId}/accept")
  @ResponseStatus(HttpStatus.CREATED)
  public MemberResponse acceptInvitation(
      @PathVariable Integer inviteId, @RequestBody AcceptInvitationRequest request) {
    var invitedMember = this.membersService.acceptInvitation(inviteId, request);
    return new MemberResponse(invitedMember);
  }
}
