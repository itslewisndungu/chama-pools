package chamapool.application.members.controllers;

import chamapool.application.members.MembersService;
import chamapool.application.members.requests.AcceptInvitationRequest;
import chamapool.application.members.requests.NewMemberRequest;
import chamapool.application.members.responses.MultipleInvitationsResponse;
import chamapool.domain.member.VOs.InvitedMemberVO;
import chamapool.domain.member.VOs.MemberProfileVO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/members/invites")
@RequiredArgsConstructor
public class MemberInvitationController {
  private final MembersService membersService;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public InvitedMemberVO inviteNewMember(@RequestBody NewMemberRequest request) {
      return  this.membersService.inviteMember(request);
  }

  @PostMapping("/{inviteId}/accept")
  @ResponseStatus(HttpStatus.CREATED)
  public MemberProfileVO acceptInvitation(
      @PathVariable Integer inviteId, @RequestBody AcceptInvitationRequest request) {
    return this.membersService.acceptInvitation(inviteId, request);
  }

  @GetMapping
  public MultipleInvitationsResponse getAllInvitations() {
    var invitations = this.membersService.getAllInvitations();
    return new MultipleInvitationsResponse(invitations);
  }

  @GetMapping("/{inviteId}")
  public InvitedMemberVO getInvitation(@PathVariable Integer inviteId) {
    return this.membersService.getInvitation(inviteId);
  }
}
