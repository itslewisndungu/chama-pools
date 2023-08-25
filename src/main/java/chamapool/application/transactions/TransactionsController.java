package chamapool.application.transactions;

import chamapool.domain.transaction.TransactionVO;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
public class TransactionsController {
    private final TransactionsService service;

    @GetMapping
    public List<TransactionVO> getTransactions() {
        return this.service.retrieveTransactions();
    }
}
