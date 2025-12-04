package com.finance.dashboard.dto.request;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class CreateHoldingRequest {
    private String ticker;
    private BigDecimal shares;
    private BigDecimal purchasePrice;
    private LocalDateTime purchaseDate;
    private String accountId;
}
