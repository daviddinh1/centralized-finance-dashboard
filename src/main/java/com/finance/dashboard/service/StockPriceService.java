package com.finance.dashboard.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class StockPriceService {
    private final RestTemplate restTemplate;

    @Value("${alphavantage.api.key}")
    private String apiKey;

    @Cacheable(value ="stockPrices", key = "#ticker")
    public BigDecimal fetchStockPrice(String ticker){
        log.info("CACHE MISS - Fetching {} from Alpha Vantage API", ticker);

        String url = String.format(
                "https://www.alphavantage.co/query?function=GLOBAL_QUOTE&symbol=%s&apikey=%s",
                ticker, apiKey
        );

        try{
            Map<String, Object> response = restTemplate.getForObject(url, Map.class);

            if (response == null || !response.containsKey("Global Quote")) {
                log.info("API Response for {}: {}", ticker, response);
                return null;
            }

            Map<String,String> quote = (Map<String, String>) response.get("Global Quote");
            String priceString = quote.get("05. price");
            return new BigDecimal(priceString);
        }catch(Exception e){
            log.error("Error fetching price for {}: {}", ticker, e.getMessage());
            return null;
        }
    }
}
