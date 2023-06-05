package chamapool.domain.member.VOs;

import chamapool.domain.member.models.Role;

public record RoleVO(String name) {
  public RoleVO(Role role) {
    this(role.name());
  }
}
