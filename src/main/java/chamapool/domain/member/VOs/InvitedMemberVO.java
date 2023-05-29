package chamapool.domain.member.VOs;

import chamapool.domain.member.models.InvitedMember;
import java.time.LocalDate;

public record InvitedMemberVO(
    Integer inviteId,
    String username,
    String firstName,
    String lastName,
    String nationalId,
    String phoneNumber,
    LocalDate invitedOn,
    String position,
    String organization,
    Double salary,
    String nextOfKinNationalId,
    String nextOfKinFirstName,
    String nextOfKinLastName,
    String nextOfKinMobileNumber,
    String county,
    String subCounty,
    String constituency) {

  public InvitedMemberVO(InvitedMember member) {
    this(
        member.id(),
        member.generatedUsername(),
        member.firstName(),
        member.lastName(),
        member.nationalId(),
        member.phoneNumber(),
        member.invitedOn(),
        member.position(),
        member.organization(),
        member.salary(),
        member.nextOfKinNationalId(),
        member.firstName(),
        member.lastName(),
        member.phoneNumber(),
        member.county(),
        member.subCounty(),
        member.constituency());
  }
}
