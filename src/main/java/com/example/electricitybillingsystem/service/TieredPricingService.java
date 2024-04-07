package com.example.electricitybillingsystem.service;

import com.example.electricitybillingsystem.model.TieredPricingEntity;
import com.example.electricitybillingsystem.repository.TieredPricingRepository;
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

}
