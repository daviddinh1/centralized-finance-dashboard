package com.finance.dashboard.dto.response;

import com.finance.dashboard.model.Holding;
import lombok.Data;
import org.springframework.cglib.core.Local;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

@Data
public class HoldingDTO {
    private String id;
    private String ticker;
    private BigDecimal shares;
    private BigDecimal purchasePrice;
    private BigDecimal currentPrice;
    private String userId;
    private String accountId;
    private LocalDateTime purchaseDate;

    //calculate values of holding
    private BigDecimal totalValue;
    private BigDecimal totalCost;
    private BigDecimal profitLoss;
    private double profitLossPercentage;

    //create a static factory method that converts my entity to a DTO
    public static HoldingDTO fromEntity(Holding holding){
        HoldingDTO dto = new HoldingDTO();

        dto.setId(holding.getId());
        dto.setTicker(holding.getTicker());
        dto.setShares(holding.getShares());
        dto.setPurchasePrice(holding.getPurchasePrice());
        //not sure how we are getting this price yet
        dto.setCurrentPrice(holding.getCurrentPrice());
        dto.setUserId(holding.getUserId());
        dto.setAccountId(holding.getAccountId());
        dto.setPurchaseDate(holding.getPurchaseDate());

        //calculate setters for values of holding
        BigDecimal totalCost = holding.getShares().multiply(holding.getPurchasePrice());
        dto.setTotalCost(totalCost);

        if(holding.getCurrentPrice() != null){
            BigDecimal totalValue = holding.getShares().multiply(holding.getCurrentPrice());
            dto.setTotalValue(totalValue);
            dto.setProfitLoss(totalValue.subtract(totalCost));

            double percentage = totalValue.subtract(totalCost)
                    .divide(totalCost, 4, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100))
                    .doubleValue();
            dto.setProfitLossPercentage(percentage);

        }
        return dto;
    }
}
