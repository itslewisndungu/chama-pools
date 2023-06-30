package chamapool.domain.member.VOs;

import chamapool.domain.member.models.MembershipFee;

public record MembershipFeeVO(Double amount, Double amountPaid, String status, Double balance) {
  public MembershipFeeVO(MembershipFee fee) {
    this(fee.amount(), fee.amountPaid(), fee.status().toString(), fee.balance());
  }
}
