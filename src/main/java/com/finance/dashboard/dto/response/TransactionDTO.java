package com.finance.dashboard.dto.response;

import com.finance.dashboard.model.Transaction;
import com.finance.dashboard.model.TransactionType;
import lombok.Data;


import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class TransactionDTO {
    private String id;
    private String userId;
    private String accountId;
    private BigDecimal amount;
    private TransactionType type;
    private String category;
    private String description;
    private LocalDateTime date;


    public static TransactionDTO fromEntity(Transaction transaction){
        TransactionDTO transactionDTO = new TransactionDTO();

        //set transctionDTO by using transction getters
        transactionDTO.setId(transaction.getId());
        transactionDTO.setUserId(transaction.getUserId());
        transactionDTO.setAccountId(transaction.getAccountId());
        transactionDTO.setAmount(transaction.getAmount());
        transactionDTO.setCategory(transaction.getCategory());
        transactionDTO.setDescription(transaction.getDescription());
        transactionDTO.setType(transaction.getType());
        transactionDTO.setDate(transaction.getDate());

        return transactionDTO;
    }
}
