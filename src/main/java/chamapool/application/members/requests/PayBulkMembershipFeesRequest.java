package chamapool.application.members.requests;

import java.util.List;

public record PayBulkMembershipFeesRequest(List<MembershipFeePayment> feePayments) {}

