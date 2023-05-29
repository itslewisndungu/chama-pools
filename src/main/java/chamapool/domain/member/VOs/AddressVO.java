package chamapool.domain.member.VOs;

import chamapool.domain.member.models.Address;

public record AddressVO(String county, String subCounty, String constituency) {
  public AddressVO(Address address) {
    this(address.county(), address.subCounty(), address.constituency());
  }
}
