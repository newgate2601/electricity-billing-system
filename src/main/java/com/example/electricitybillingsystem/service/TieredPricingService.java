package com.example.electricitybillingsystem.service;

import com.example.electricitybillingsystem.model.TieredPricingEntity;
import com.example.electricitybillingsystem.repository.TieredPricingRepository;
import com.example.electricitybillingsystem.vo.request.UpdatePriceRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TieredPricingService {
    private final TieredPricingRepository tieredPricingRepository;

    List<TieredPricingEntity> getAllByServiceId(Long id) {
        return tieredPricingRepository.findAllByElectricityServiceId(id);
    }

    @Transactional
    public Object updatePrice(UpdatePriceRequest request) {
        tieredPricingRepository.deleteTieredPricingEntitiesByIdIn(request.getCurrentPrices().stream().map(TieredPricingEntity::getId).toList());
        tieredPricingRepository.saveAll(request.getNewPrices());
        return true;
    }

}
