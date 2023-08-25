package chamapool.domain.member.VOs;

import chamapool.domain.member.enums.MemberRole;
import chamapool.domain.member.enums.Status;
import chamapool.domain.member.models.Member;
import chamapool.domain.member.models.Role;
import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.Data;

@Data
public class MemberVO {
  private final Integer id;
  private final String firstName;
  private final String lastName;
  private final String username;
  private final String nationalId;
  private final String phoneNumber;
  private final LocalDate joinedOn;
  private final Status status;
  private final Set<MemberRole> roles;

  public MemberVO(Member member) {
    this.id = member.id();
    this.firstName = member.firstName();
    this.lastName = member.lastName();
    this.username = member.username();
    this.nationalId = member.nationalId();
    this.phoneNumber = member.phoneNumber();
    this.joinedOn = member.joinedOn();
    this.status = member.status();
    this.roles = member.roles().stream().map(Role::name).collect(Collectors.toSet());
  }
}
