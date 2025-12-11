package com.finance.dashboard.repository;

import com.finance.dashboard.model.Transaction;
import com.finance.dashboard.model.TransactionType;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface TransactionRepository extends MongoRepository<Transaction,String> {
    List<Transaction> findByUserId(String userId);
    List<Transaction> findByUserIdAndType(String userId, TransactionType type);
    List<Transaction> findByUserIdAndCategory(String userId, String category);
    List<Transaction> findTransactionByDateBetween(LocalDateTime dateAfter, LocalDateTime dateBefore);
}
