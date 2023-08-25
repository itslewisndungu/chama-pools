package chamapool.application.loans.responses;

import chamapool.domain.loans.VO.LoanInstallmentVO;
import java.util.List;

public record LoanInstallmentsResponse(
    List<LoanInstallmentVO> installments, Double loanBalance, Double totalPaid) {}
