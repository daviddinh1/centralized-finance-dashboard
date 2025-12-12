package com.finance.dashboard.controller;

import com.finance.dashboard.dto.request.CreateTransactionRequest;
import com.finance.dashboard.dto.response.MonthlySummaryDTO;
import com.finance.dashboard.dto.response.TransactionDTO;
import com.finance.dashboard.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;

    @PostMapping
    public ResponseEntity<TransactionDTO> createTransaction(@Valid @RequestBody CreateTransactionRequest request, @AuthenticationPrincipal UserDetails userDetails){
        String userId = userDetails.getUsername();
        TransactionDTO transaction = transactionService.createTransaction(request, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(transaction);
    }

    @GetMapping
    public ResponseEntity<List<TransactionDTO>> getAllUserTransactions(@AuthenticationPrincipal UserDetails userDetails){
        String userId = userDetails.getUsername();
        List<TransactionDTO> transactionDTO = transactionService.getAllTransactions(userId);
        return ResponseEntity.ok(transactionDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionDTO> getSpecificTransaction(@PathVariable String id, @AuthenticationPrincipal UserDetails userDetails){
        String userId = userDetails.getUsername();
        TransactionDTO transactionDTO = transactionService.getTransactionById(id, userId);
        return ResponseEntity.ok(transactionDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TransactionDTO> updateTransaction(@PathVariable String id, @Valid @RequestBody CreateTransactionRequest request, @AuthenticationPrincipal UserDetails userDetails){
        String userId = userDetails.getUsername();
        TransactionDTO transactionDTO = transactionService.updateTransactionById(id, request, userId);
        return ResponseEntity.ok(transactionDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable String id, @AuthenticationPrincipal UserDetails userdetails){
        String userId = userdetails.getUsername();
        transactionService.deleteTransaction(id, userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/summary")
    public ResponseEntity<MonthlySummaryDTO> getUserMonthlyTransactionSummary(@AuthenticationPrincipal UserDetails userDetails, @RequestParam int month, @RequestParam int year){
        String userId = userDetails.getUsername();
        MonthlySummaryDTO monthlySummaryDTO = transactionService.getMonthlySummary(userId, month, year);
        return ResponseEntity.ok(monthlySummaryDTO);
    }
}
