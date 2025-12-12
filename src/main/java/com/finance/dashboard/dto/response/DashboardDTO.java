package com.finance.dashboard.dto.response;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class DashboardDTO {
    private BigDecimal totalPortfolioValue;
    private BigDecimal totalIncome;
    private BigDecimal totalExpenses;
    private BigDecimal cashBalance; //income - expense
    private BigDecimal netWorth; //portfolio + cashBalance

    private List<TransactionDTO> recentTransactions;
    private List<HoldingDTO> holdings;
}
