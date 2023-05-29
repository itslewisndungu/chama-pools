package chamapool.domain.member.VOs;

import chamapool.domain.member.models.Occupation;

public record OccupationVO(String organization, String position, Double salary) {
  public OccupationVO(Occupation occupation) {
    this(occupation.organization(), occupation.position(), occupation.salary());
  }
}
