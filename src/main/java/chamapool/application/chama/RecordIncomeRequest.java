package chamapool.application.chama;

import java.util.List;

public record RecordIncomeRequest(List<Income> incomes) {}

record Income(Double amount, String description) {}
