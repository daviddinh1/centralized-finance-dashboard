package com.finance.dashboard.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Document(collection = "price_history")
@CompoundIndex(name = "ticker_date_idx", def = "{'ticker':1, 'date': -1}")
public class PriceHistory {
    @Id
    private String id;

    private String ticker;
    private BigDecimal price;
    private LocalDateTime date = LocalDateTime.now();
}
