package com.finance.dashboard.dto.request;

import com.finance.dashboard.model.TransactionType;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class CreateTransactionRequest {
    @NotNull
    @DecimalMin("0.01")
    private BigDecimal amount;
    @NotNull
    private TransactionType type;
    @NotNull
    private String category;
    private String description;
    private String accountId;
    private LocalDateTime date = LocalDateTime.now();
}
