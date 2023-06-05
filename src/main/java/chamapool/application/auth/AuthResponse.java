package chamapool.application.auth;

import chamapool.domain.member.VOs.MemberVO;
import chamapool.domain.member.models.Member;

public class AuthResponse {
  public MemberVO member;
  public String token;

  public AuthResponse(Member member) {
    this.member = new MemberVO(member);
    this.token = member.token();
  }
}
