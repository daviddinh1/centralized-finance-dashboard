package com.finance.dashboard.scheduler;

import com.finance.dashboard.model.Holding;
import com.finance.dashboard.repository.HoldingRepository;
import com.finance.dashboard.repository.PriceHistoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import lombok.RequiredArgsConstructor;
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

    @Scheduled(cron = "0 * * * * *")
    public void UpdateStockPrices(){
        log.info("UpdateStockPrices -- Price update started");
        List<Holding> allHoldings = holdingRepository.findAll();

        //extract unique tickers
        List<String> uniqueTickers = allHoldings.stream().map(Holding::getTicker).distinct().toList();

        for(String ticker : uniqueTickers){
            try{
                String url = String.format(
                        "https://www.alphavantage.co/query?function=GLOBAL_QUOTE&symbol=%s&apikey=%s",
                        ticker,
                        apiKey
                );
                Map<String,Object> response = restTemplate.getForObject(url, Map.class);

                //extract price from json structure
                Map<String, String> quote = (Map<String, String>) response.get("Global Quote");
                String priceString = quote.get("05. price");
                BigDecimal price = new BigDecimal(priceString);

                List<Holding> holdings = holdingRepository.findByTicker(ticker);
                for(Holding holding : holdings){
                    holding.setCurrentPrice(price);
                }
                holdingRepository.saveAll(holdings);

                //rate limiter
                Thread.sleep(12000);
            } catch(Exception e){
                log.error("Error updating ticker {}: {}", ticker, e.getMessage());

            }
            log.info("UpdateStockPrices -- Price update completed");

        }

    }
}
