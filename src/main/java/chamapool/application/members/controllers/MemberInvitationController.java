package chamapool.application.members.controllers;

import chamapool.application.members.MembersService;
import chamapool.application.members.requests.AcceptInvitationRequest;
import chamapool.application.members.requests.NewMemberRequest;
import chamapool.application.members.responses.InviteMemberResponse;
import chamapool.application.members.responses.MemberProfileResponse;
import chamapool.application.members.responses.MultipleInvitationsResponse;
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
  //  @PreAuthorize("hasAnyRole('ROLE_CHAIRMAN', 'ROLE_SECRETARY')")
  public InviteMemberResponse inviteNewMember(@RequestBody NewMemberRequest request) {
    var invitedMember = this.membersService.inviteMember(request);
    return new InviteMemberResponse(invitedMember);
  }

  @PostMapping("/{inviteId}/accept")
  @ResponseStatus(HttpStatus.CREATED)
  public MemberProfileResponse acceptInvitation(
      @PathVariable Integer inviteId, @RequestBody AcceptInvitationRequest request) {
    var invitedMember = this.membersService.acceptInvitation(inviteId, request);
    return new MemberProfileResponse(invitedMember);
  }

  @GetMapping
  public MultipleInvitationsResponse getAllInvitations() {
    var invitations = this.membersService.getAllInvitations();
    return new MultipleInvitationsResponse(invitations);
  }

  @GetMapping("/{inviteId}")
  public InviteMemberResponse getInvitation(@PathVariable Integer inviteId) {
    var invitedMember = this.membersService.getInvitation(inviteId);
    return new InviteMemberResponse(invitedMember);
  }
}
