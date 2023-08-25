package chamapool.domain.member.VOs;

import chamapool.domain.member.models.MembershipFee;

public record MembershipFeeVO(
    Integer memberId,
    String memberName,
    Double amount,
    Double amountPaid,
    String status,
    Double balance) {
  public MembershipFeeVO(MembershipFee fee) {
    this(
        fee.member().id(),
        fee.member().fullName(),
        fee.amount(),
        fee.amountPaid(),
        fee.status().toString(),
        fee.balance());
  }
}
