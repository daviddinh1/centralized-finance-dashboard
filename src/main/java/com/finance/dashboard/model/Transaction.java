package com.finance.dashboard.model;

import lombok.Data;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Document(collection = "transactions")
public class Transaction {
    @Id
    private String id;

    @Indexed
    private String userId;
    private String accountId;
    private BigDecimal amount;
    private TransactionType type;
    private String category;
    private String description;
    private LocalDateTime date = LocalDateTime.now();


}
