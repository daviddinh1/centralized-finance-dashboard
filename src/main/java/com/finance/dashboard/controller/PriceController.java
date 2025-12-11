package com.finance.dashboard.controller;

import com.finance.dashboard.scheduler.PriceUpdateScheduler;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/price")
@RequiredArgsConstructor
public class PriceController {
    private final PriceUpdateScheduler priceUpdateScheduler;

    @PostMapping
    public ResponseEntity<Void> updateStockPrices(){
        new Thread(() ->priceUpdateScheduler.updateStockPrices()).start(); //instead of waiting for the entire job to finish we just run it in the background
        return ResponseEntity.noContent().build();
    }
}
