package com.finance.dashboard.dto.response;

import lombok.Data;
import org.springframework.cglib.core.Local;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class HoldingDTO {
    private String id;
    private String ticker;
    private BigDecimal shares;
    private BigDecimal purchasePrice;
    private BigDecimal currentPrice;
    private String userId;
    private String accountId;
    private LocalDateTime purchaseDate;

    //calculate values of holding
    private BigDecimal totalValue;
    private BigDecimal totalCost;
    private BigDecimal profitLoss;
    private float profitLossPercentage;
}
