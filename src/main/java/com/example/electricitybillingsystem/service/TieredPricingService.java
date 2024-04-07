package com.example.electricitybillingsystem.service;

import com.example.electricitybillingsystem.model.TieredPricing;
import com.example.electricitybillingsystem.repository.TieredPricingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TieredPricingService {
    private final TieredPricingRepository tieredPricingRepository;

    List<TieredPricing> getAllByServiceId(Long id) {
        return tieredPricingRepository.findAllByServiceId(id);
    }

}
