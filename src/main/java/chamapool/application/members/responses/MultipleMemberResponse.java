package chamapool.application.members.responses;

import chamapool.domain.member.VOs.MemberVO;

import java.util.List;

public record MultipleMemberResponse(List<MemberVO> members) {}
