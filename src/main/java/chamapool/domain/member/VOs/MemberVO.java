package chamapool.domain.member.VOs;

import chamapool.domain.member.enums.Role;
import chamapool.domain.member.enums.Status;
import chamapool.domain.member.models.Member;
import java.time.LocalDate;

public record MemberVO(
    Integer id,
    String firstName,
    String lastName,
    String username,
    String nationalId,
    String phoneNumber,
    LocalDate joinedOn,
    Status status,
    Role role,
    NextOfKinVO nextOfKin,
    OccupationVO occupation,
    AddressVO homeAddress) {
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
        member.role(),
        new NextOfKinVO(member.nextOfKin()),
        new OccupationVO(member.occupation()),
        new AddressVO(member.homeAddress()));
  }
}
