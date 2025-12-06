package com.finance.dashboard.service;

import com.finance.dashboard.dto.request.CreateHoldingRequest;
import com.finance.dashboard.dto.response.HoldingDTO;
import com.finance.dashboard.dto.response.PortfolioSummaryDTO;
import com.finance.dashboard.model.Holding;
import com.finance.dashboard.repository.HoldingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HoldingService {
    private final HoldingRepository holdingRepository;

    //create a holding to our db
    public HoldingDTO createHolding(CreateHoldingRequest request, String userId){
        Holding holding = new Holding();

        holding.setTicker(request.getTicker());
        holding.setShares(request.getShares());
        holding.setPurchasePrice(request.getPurchasePrice());
        holding.setUserId(userId);
        holding.setAccountId(request.getAccountId());

        Holding savedHolding = holdingRepository.save(holding);

        return HoldingDTO.fromEntity(savedHolding);
    }

    //get all holdings for a user
    public List<HoldingDTO> getAllHoldings(String userId){
        List<Holding> holdings = holdingRepository.findByUserId(userId);

        //use a stream to map it into our list
        return holdings.stream()
                .map(HoldingDTO::fromEntity) //:: basically means using this method
                .collect(Collectors.toList());
    }

    //get holding by id
    public HoldingDTO getHoldingById(String id, String userId){
        //get holding by id
        Holding holding = holdingRepository.findById(id).orElseThrow(() -> new RuntimeException("Holding not found"));
        //check if the holding belong to the user
        if(!holding.getUserId().equals(userId)){
            throw new RuntimeException("Unauthorized - not your holding!");
        }

        //convert to dto and return
        return HoldingDTO.fromEntity(holding);
    }

    //update a holding
    public HoldingDTO updateHolding(String id, CreateHoldingRequest request, String userId){
        Holding holding = holdingRepository.findById(id).orElseThrow(() -> new RuntimeException("Holding not found"));

        if(!holding.getUserId().equals(userId)){
            throw new RuntimeException("Unauthorized - not your holding!");
        }

        holding.setTicker(request.getTicker());
        holding.setShares(request.getShares());
        holding.setPurchasePrice(request.getPurchasePrice());
        holding.setAccountId(request.getAccountId());
        if (request.getPurchaseDate() != null) {
            holding.setPurchaseDate(request.getPurchaseDate());
        }


        Holding savedHolding = holdingRepository.save(holding);

        return HoldingDTO.fromEntity(savedHolding);
    }

    //delete a holding
    public void deleteHolding(String id, String userId){
        Holding holding = holdingRepository.findById(id).orElseThrow(() -> new RuntimeException("Holding not found"));

        if(!holding.getUserId().equals(userId)){
            throw new RuntimeException("Unauthorized - not your holding!");
        }

        holdingRepository.deleteById(id);
    }

    //get portfolioSummary
    public PortfolioSummaryDTO getPortfolioSummary(String userId){
        //get all the users holdings
        List<Holding> holdings = holdingRepository.findByUserId(userId);

        //go through each holding and convert it to holdingDTO
        List<HoldingDTO> holdingDTOs = holdings.stream().map(HoldingDTO::fromEntity).toList();

        //calculate totalValue (shares * currentPrice)
        BigDecimal totalValue = holdingDTOs.stream().map(HoldingDTO:: getTotalValue)
                .filter(value -> value != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add); //(accumulator is where u start, function you want to use)

        //calculate totalCost (shares * purchasePrice)
        BigDecimal totalCost = holdingDTOs.stream().map(HoldingDTO::getTotalCost).filter(value -> value != null).reduce(BigDecimal.ZERO, BigDecimal::add);

        //calculate totalProfitLoss (totalVal - totalCost)
        BigDecimal totalProfitLoss = totalValue.subtract(totalCost);

        //calculateProfitLossPercentage (totalProfitLoss / totalCost) * 100
        double percentage = 0.0;
        if (totalCost.compareTo(BigDecimal.ZERO) > 0) {
            percentage = totalProfitLoss  // Use the one you already calculated!
                    .divide(totalCost, 4, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100))
                    .doubleValue();
        }

        //create and return the portfolioSummaryDTO
        PortfolioSummaryDTO portfolioSummaryDTO = new PortfolioSummaryDTO();
        portfolioSummaryDTO.setHoldings(holdingDTOs);
        portfolioSummaryDTO.setTotalValue(totalValue);
        portfolioSummaryDTO.setTotalCost(totalCost);
        portfolioSummaryDTO.setTotalProfitLoss(totalProfitLoss);
        portfolioSummaryDTO.setProfitLossPercentage(percentage);

        return portfolioSummaryDTO;
    }

    //convert holding entity to holdingDto
}
