package com.finance.dashboard.service;

import com.finance.dashboard.dto.request.CreateHoldingRequest;
import com.finance.dashboard.dto.response.HoldingDTO;
import com.finance.dashboard.model.Holding;
import com.finance.dashboard.repository.HoldingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

        //use a steam to map it into our list
        return holdings.stream()
                .map(HoldingDTO::fromEntity) //:: basically means using this method
                .collect(Collectors.toList());
    }

    //get holding by id

    //update a holding

    //delete a holding

    //get portfolioSummary

    //convert holding entity to holdingDto
}
