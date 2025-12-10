package com.finance.dashboard.repository;

import com.finance.dashboard.model.Holding;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface HoldingRepository extends MongoRepository<Holding,String> {
    List<Holding> findByUserId(String userId);
    List<Holding> findByUserIdAndTicker(String userId, String ticker);
    List<Holding> findByTicker(String ticker);
}
