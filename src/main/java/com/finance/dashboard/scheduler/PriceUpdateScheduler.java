package com.finance.dashboard.scheduler;

import com.finance.dashboard.model.Holding;
import com.finance.dashboard.model.PriceHistory;
import com.finance.dashboard.repository.HoldingRepository;
import com.finance.dashboard.repository.PriceHistoryRepository;
import com.finance.dashboard.service.StockPriceService;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;



@Slf4j
@Component
@RequiredArgsConstructor
public class PriceUpdateScheduler {

    //inject repositories we need
    private final HoldingRepository holdingRepository;
    private final PriceHistoryRepository priceHistoryRepository;
    private final StockPriceService stockPriceService;

    @Scheduled(cron = "0 0 9 * * *")
    public void updateStockPrices(){
        log.info("UpdateStockPrices -- Price update started");
        List<Holding> allHoldings = holdingRepository.findAll();

        //extract unique tickers
        List<String> uniqueTickers = allHoldings.stream().map(Holding::getTicker).distinct().toList();

        for(String ticker : uniqueTickers){
            try{
                BigDecimal price = stockPriceService.fetchStockPrice(ticker);

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
}
