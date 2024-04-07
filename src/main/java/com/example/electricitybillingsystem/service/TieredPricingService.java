package com.example.electricitybillingsystem.service;

import com.example.electricitybillingsystem.model.TieredPricingEntity;
import com.example.electricitybillingsystem.repository.TieredPricingRepository;
import com.example.electricitybillingsystem.vo.request.UpdatePriceRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TieredPricingService {
    private final TieredPricingRepository tieredPricingRepository;

    List<TieredPricingEntity> getAllByServiceId(Long id) {
        return tieredPricingRepository.findAllById(id);
    }

    public Object updatePrice(UpdatePriceRequest request) {
        tieredPricingRepository.deleteTieredPricingEntitiesByIdIn(
                request.getCurrentPrices().stream()
                        .map(TieredPricingEntity::getId)
                        .filter(id -> request.getNewPrices().stream().filter(newPrice -> newPrice.getId().equals(id)).toList().isEmpty()).toList());
        tieredPricingRepository.saveAll(request.getNewPrices());
        return true;
    }

}
