package com.finance.dashboard.repository;

import com.finance.dashboard.model.PriceHistory;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface PriceHistoryRepository extends MongoRepository<PriceHistory,String> {
    List<PriceHistory> findByTicker(String ticker);
    PriceHistory findFirstByTickerOrderByDateDesc(String ticker);
    List<PriceHistory> findPriceHistoryByDateBetween(LocalDateTime startDate, LocalDateTime endDate);


}
