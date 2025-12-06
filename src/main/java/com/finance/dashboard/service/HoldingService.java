package com.finance.dashboard.service;

import com.finance.dashboard.dto.request.CreateHoldingRequest;
import com.finance.dashboard.dto.response.HoldingDTO;
import com.finance.dashboard.model.Holding;
import com.finance.dashboard.repository.HoldingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
        holding.setPurchaseDate(request.getPurchaseDate());
        holding.setUserId(userId);
        holding.setAccountId(request.getAccountId());

        Holding savedHolding = holdingRepository.save(holding);

        return HoldingDTO.fromEntity(savedHolding);
    }

    //get all holdings for a user


    //get holding by id

    //update a holding

    //delete a holding

    //get portfolioSummary

    //convert holding entity to holdingDto
}
