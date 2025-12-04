package com.finance.dashboard.dto.response;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class PortfolioSummaryDto {
    private BigDecimal totalValue;
    private BigDecimal totalCost;
    private BigDecimal totalProfitLoss;
    private Double profitLossPercentage;
    private List<HoldingDTO> holdings;
}
