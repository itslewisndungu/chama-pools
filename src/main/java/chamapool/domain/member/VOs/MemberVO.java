package chamapool.domain.member.VOs;

import chamapool.domain.member.enums.MemberRole;
import chamapool.domain.member.enums.Status;
import chamapool.domain.member.models.Member;
import chamapool.domain.member.models.Role;
import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

public record MemberVO(
    Integer id,
    String firstName,
    String lastName,
    String username,
    String nationalId,
    String phoneNumber,
    LocalDate joinedOn,
    Status status,
    Set<MemberRole> roles) {
  public MemberVO(Member member) {
    this(
        member.id(),
        member.firstName(),
        member.lastName(),
        member.username(),
        member.nationalId(),
        member.phoneNumber(),
        member.joinedOn(),
        member.status(),
        member.roles().stream().map(Role::name).collect(Collectors.toSet()));
  }
}
