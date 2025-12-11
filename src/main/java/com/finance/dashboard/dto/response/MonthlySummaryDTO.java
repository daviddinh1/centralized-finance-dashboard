package com.finance.dashboard.dto.response;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class MonthlySummaryDTO {
    private BigDecimal totalIncome;
    private BigDecimal totalExpenses;
    private BigDecimal netAmount;
    private int month;
    private int year;
    private List<TransactionDTO> transactions; //transactions for the month
}
