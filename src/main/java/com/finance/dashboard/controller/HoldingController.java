package com.finance.dashboard.controller;

import com.finance.dashboard.dto.request.CreateHoldingRequest;
import com.finance.dashboard.dto.response.HoldingDTO;
import com.finance.dashboard.dto.response.PortfolioSummaryDTO;
import com.finance.dashboard.service.HoldingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/holdings")
@RequiredArgsConstructor
public class HoldingController {
    private final HoldingService holdingService;

    @PostMapping
    public ResponseEntity<HoldingDTO> createHolding(@RequestBody CreateHoldingRequest request, @AuthenticationPrincipal UserDetails userDetails){
        String userId = userDetails.getUsername();
        HoldingDTO holding = holdingService.createHolding(request, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(holding);
    }

    @GetMapping
    public ResponseEntity<List<HoldingDTO>> getAllHoldings(@AuthenticationPrincipal UserDetails userDetails){
        String userId = userDetails.getUsername();
        List<HoldingDTO> holdings = holdingService.getAllHoldings(userId);

        return ResponseEntity.ok(holdings);
    }

    @GetMapping("/{id}")
    public ResponseEntity<HoldingDTO> getHoldingById(@PathVariable String id, @AuthenticationPrincipal UserDetails userDetails){
        String userId = userDetails.getUsername();
        HoldingDTO holding = holdingService.getHoldingById(id,userId);
        return ResponseEntity.ok(holding);
    }

    @PutMapping("/{id}")
    public ResponseEntity<HoldingDTO> updateHolding(@PathVariable String id, @RequestBody CreateHoldingRequest request, @AuthenticationPrincipal UserDetails userDetails){
        String userId = userDetails.getUsername();
        HoldingDTO updatedHolding = holdingService.updateHolding(id,request,userId);
        return ResponseEntity.ok(updatedHolding);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHolding(@PathVariable String id, @AuthenticationPrincipal UserDetails userDetails){
        String userId = userDetails.getUsername();
        holdingService.deleteHolding(id, userId);
        return ResponseEntity.noContent().build(); //we use build because want to return nothing
    }

    @GetMapping("/summary")
    public ResponseEntity<PortfolioSummaryDTO> getPortfolioSummary(@AuthenticationPrincipal UserDetails userDetails){
        String userId = userDetails.getUsername();
        PortfolioSummaryDTO portfolioSummary =  holdingService.getPortfolioSummary(userId);
        return ResponseEntity.ok(portfolioSummary);
    }

}
