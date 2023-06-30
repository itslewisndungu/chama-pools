package chamapool.domain.member.VOs;

import chamapool.domain.member.enums.MemberRole;
import chamapool.domain.member.models.Role;

public record RoleVO(MemberRole name) {
  public RoleVO(Role role) {
    this(role.name());
  }
}
