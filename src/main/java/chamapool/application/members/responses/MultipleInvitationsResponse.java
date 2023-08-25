package chamapool.application.members.responses;

import chamapool.domain.member.VOs.InvitedMemberVO;

import java.util.List;

public record MultipleInvitationsResponse(List<InvitedMemberVO> invitations) {}
