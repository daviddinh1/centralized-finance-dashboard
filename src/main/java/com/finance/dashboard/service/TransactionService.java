package com.finance.dashboard.service;

import com.finance.dashboard.dto.request.CreateTransactionRequest;
import com.finance.dashboard.dto.response.MonthlySummaryDTO;
import com.finance.dashboard.dto.response.TransactionDTO;
import com.finance.dashboard.model.Transaction;
import com.finance.dashboard.model.TransactionType;
import com.finance.dashboard.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionRepository transactionRepository;

    //create transaction
    public TransactionDTO createTransaction(CreateTransactionRequest request, String userId){
        Transaction transaction = new Transaction();
        transaction.setUserId(userId);
        transaction.setAccountId(request.getAccountId());
        transaction.setAmount(request.getAmount());
        transaction.setType(request.getType());
        transaction.setCategory(request.getCategory());
        transaction.setDescription(request.getDescription());
        transaction.setDate(request.getDate());

        Transaction saveTransaction = transactionRepository.save(transaction);

        return TransactionDTO.fromEntity(saveTransaction);

    }

    //get all transaction of a user
    public List<TransactionDTO> getAllTransactions(String userId){
        List<Transaction> transactions = transactionRepository.findByUserIdOrderByDateDesc(userId);

        return transactions.stream().map(TransactionDTO::fromEntity).toList();
    }

    //get specific transaction by id
    public TransactionDTO getTransactionById(String id, String userId){
        Transaction transaction = transactionRepository.findById(id).orElseThrow(() -> new RuntimeException("Transaction does not exist"));

        //check if its the users transaction
        if(!transaction.getUserId().equals(userId)){
            throw new RuntimeException("Unauthorized - not your transaction!");
        }

        return TransactionDTO.fromEntity(transaction);
    }

    //update transaction by id
    public TransactionDTO updateTransactionById(CreateTransactionRequest request, String id, String userId){
        Transaction transaction = transactionRepository.findById(id).orElseThrow(() -> new RuntimeException("Transaction does not exist"));

        if(!transaction.getUserId().equals(userId)){
            throw new RuntimeException("Unauthorized - not your transaction!");
        }

        //update transaction
        transaction.setAccountId(request.getAccountId());
        transaction.setAmount(request.getAmount());
        transaction.setType(request.getType());
        transaction.setDate(request.getDate());
        transaction.setCategory(request.getCategory());
        transaction.setDescription(request.getDescription());

        Transaction saveTransaction = transactionRepository.save(transaction);

        return TransactionDTO.fromEntity(saveTransaction);
    }

    //delete transaction
    public void deleteTransaction(String id, String userId){
        Transaction transaction = transactionRepository.findById(id).orElseThrow(() -> new RuntimeException("Transaction does not exist"));

        if(!transaction.getUserId().equals(userId)){
            throw new RuntimeException("Unauthorized - not your transaction!");
        }

        transactionRepository.deleteById(id);
    }

    //get monthly summary
    public MonthlySummaryDTO getMonthlySummary(String userId, int month, int year){
        LocalDateTime startOfMonth = LocalDateTime.of(year, month, 1, 0, 0);
        LocalDateTime endOfMonth = startOfMonth.plusMonths(1).minusSeconds(1);

        List<Transaction> transactions = transactionRepository.findByUserIdAndDateBetween(userId, startOfMonth, endOfMonth);
        List<TransactionDTO> transactionDTOs = transactions.stream().map(TransactionDTO::fromEntity).toList();

        //get total income
        BigDecimal totalIncome = transactionDTOs.stream().filter(t -> t.getType() == TransactionType.INCOME).map(TransactionDTO::getAmount).reduce(BigDecimal.ZERO,BigDecimal::add);
        //get total expenses
        BigDecimal totalExpense = transactionDTOs.stream().filter(t -> t.getType() == TransactionType.EXPENSE).map(TransactionDTO::getAmount).reduce(BigDecimal.ZERO,BigDecimal::add);

        //get net worth of month
        BigDecimal netAmount = totalIncome.subtract(totalExpense);

        MonthlySummaryDTO monthlySummaryDTO = new MonthlySummaryDTO();
        monthlySummaryDTO.setTransactions(transactionDTOs);
        monthlySummaryDTO.setYear(year);
        monthlySummaryDTO.setMonth(month);
        monthlySummaryDTO.setNetAmount(netAmount);
        monthlySummaryDTO.setTotalIncome(totalIncome);
        monthlySummaryDTO.setTotalExpenses(totalExpense);

        return monthlySummaryDTO;

    }
}
