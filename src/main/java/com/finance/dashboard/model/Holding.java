package com.finance.dashboard.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Document(collection = "holdings")
public class Holding {
    @Id
    private String id;

    private String ticker;
    private BigDecimal shares;
    private BigDecimal purchasePrice;
    private BigDecimal currentPrice = null;
    private LocalDateTime purchaseDate = LocalDateTime.now();

    @Indexed //create a faster query
    private String userId;
    private String accountId;
}
