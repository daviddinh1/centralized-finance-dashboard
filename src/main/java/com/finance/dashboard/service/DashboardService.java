package com.finance.dashboard.service;

import com.finance.dashboard.dto.response.DashboardDTO;
import com.finance.dashboard.dto.response.HoldingDTO;
import com.finance.dashboard.dto.response.PortfolioSummaryDTO;
import com.finance.dashboard.dto.response.TransactionDTO;
import com.finance.dashboard.model.TransactionType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardService {
    private final HoldingService holdingService;
    private final TransactionService transactionService;

    public DashboardDTO getDashboard(String userId){
        //get all holdings of a user
        List<HoldingDTO> holdings = holdingService.getAllHoldings(userId);
        //get all transactions of a user
        List<TransactionDTO> transactions = transactionService.getAllTransactions(userId);

        //need to get income and expenses
        BigDecimal totalIncome = transactions.stream().filter(t -> t.getType() == TransactionType.INCOME).map(TransactionDTO::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalExpenses = transactions.stream().filter(t -> t.getType() == TransactionType.EXPENSE).map(TransactionDTO::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);

        //get total portfolio value utilize portfolioSummary from holdings
        PortfolioSummaryDTO portfolioSummaryDTO = holdingService.getPortfolioSummary(userId);
        BigDecimal totalPortfolioValue = portfolioSummaryDTO.getTotalValue();

        //get cashBalance
        BigDecimal cashBalance = totalIncome.subtract(totalExpenses);

        //get networth
        BigDecimal netWorth = totalPortfolioValue.add(cashBalance);

        DashboardDTO dashboardDTO = new DashboardDTO();
        dashboardDTO.setHoldings(holdings);
        dashboardDTO.setRecentTransactions(transactions.stream().limit(10).toList());
        dashboardDTO.setTotalIncome(totalIncome);
        dashboardDTO.setTotalExpenses(totalExpenses);
        dashboardDTO.setTotalPortfolioValue(totalPortfolioValue);
        dashboardDTO.setCashBalance(cashBalance);
        dashboardDTO.setNetWorth(netWorth);

        return dashboardDTO;
    }
}
