package com.finance.dashboard.scheduler;

import com.finance.dashboard.model.Holding;
import com.finance.dashboard.model.PriceHistory;
import com.finance.dashboard.repository.HoldingRepository;
import com.finance.dashboard.repository.PriceHistoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;


@Slf4j
@Component
@RequiredArgsConstructor
public class PriceUpdateScheduler {

    //inject repositories we need
    private final HoldingRepository holdingRepository;
    private final PriceHistoryRepository priceHistoryRepository;
    private final RestTemplate restTemplate;

    //inject api key from properties
    @Value("${alphavantage.api.key}")
    private String apiKey;

    @Scheduled(cron = "0 0 9 * * *")
    public void updateStockPrices(){
        log.info("UpdateStockPrices -- Price update started");
        List<Holding> allHoldings = holdingRepository.findAll();

        //extract unique tickers
        List<String> uniqueTickers = allHoldings.stream().map(Holding::getTicker).distinct().toList();

        for(String ticker : uniqueTickers){
            try{
                BigDecimal price = fetchStockPrice(ticker);

                if(price == null){
                    log.error("Failed to fetch price for {}", ticker);
                    continue; //skip to the next ticker
                }

                List<Holding> holdings = holdingRepository.findByTicker(ticker);
                for(Holding holding : holdings){
                    holding.setCurrentPrice(price);
                }
                holdingRepository.saveAll(holdings);

                PriceHistory priceHistory = new PriceHistory();
                priceHistory.setPrice(price);
                priceHistory.setTicker(ticker);
                priceHistoryRepository.save(priceHistory);

                //rate limiter
                Thread.sleep(12000);
            } catch(Exception e){
                log.error("Error updating ticker {}: {}", ticker, e.getMessage());

            }
        }
        log.info("UpdateStockPrices -- Price update completed");
    }

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
                log.error("Invalid response for ticker {}", ticker);
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
