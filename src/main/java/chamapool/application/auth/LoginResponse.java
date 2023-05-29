package chamapool.application.auth;

import chamapool.domain.member.models.Member;
import chamapool.domain.member.enums.Role;

public class LoginResponse {
    public String username;
    public Role role;
    public String token;

    public LoginResponse(Member member) {
        this.username = member.username();
        this.role = member.role();
        this.token = member.token();
    }
}