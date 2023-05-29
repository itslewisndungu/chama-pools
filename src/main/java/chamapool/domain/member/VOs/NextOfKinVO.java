package chamapool.domain.member.VOs;

import chamapool.domain.member.models.NextOfKin;

public record NextOfKinVO(
    String firstName, String lastName, String nationalId, String mobileNumber) {

  public NextOfKinVO(NextOfKin kin) {
    this(kin.firstName(), kin.lastName(), kin.nationalId(), kin.mobileNumber());
  }
}
