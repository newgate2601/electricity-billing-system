package com.example.electricitybillingsystem.service;

import com.example.electricitybillingsystem.model.TieredPricingHistory;
import com.example.electricitybillingsystem.model.TieredPricingHistoryBill;
import com.example.electricitybillingsystem.repository.TieredPricingHistoryBillRepository;
import com.example.electricitybillingsystem.repository.TieredPricingHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TieredPricingHistoryService {
    private final TieredPricingHistoryRepository tieredPricingHistoryRepository;
    private final TieredPricingHistoryBillRepository tieredPricingHistoryBillRepository;

    public List<TieredPricingHistory> getAllTieredPricingHistoryByBillId(Long id){
        List<TieredPricingHistoryBill> tieredPricingHistoryBills = tieredPricingHistoryBillRepository.findAllByBillId(id);
        if(tieredPricingHistoryBills==null || tieredPricingHistoryBills.size()==0){
            return null;
        }

        List<Long> ids = tieredPricingHistoryBills.stream().map(TieredPricingHistoryBill::getBillId).collect(Collectors.toList());
        if(ids==null || ids.size()==0){
            return null;
        }

        List<TieredPricingHistory> tieredPricingHistories = tieredPricingHistoryRepository.findAllByIdIn(ids);

        return tieredPricingHistories;

    }
}
