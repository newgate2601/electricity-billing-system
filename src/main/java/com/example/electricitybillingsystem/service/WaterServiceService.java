package com.example.electricitybillingsystem.service;

import com.example.electricitybillingsystem.mapper.WaterServiceMapper;
import com.example.electricitybillingsystem.model.WaterServiceEntity;
import com.example.electricitybillingsystem.repository.WaterServiceRepository;
import com.example.electricitybillingsystem.vo.response.WaterServiceResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WaterServiceService {
    private final WaterServiceRepository waterServiceRepository;
    private final TieredPricingService tieredPricingService;
    private final WaterServiceMapper electricityServiceMapper;

    public WaterServiceEntity create(WaterServiceEntity waterService) {
        return waterServiceRepository.save(waterService);
    }

    public List<WaterServiceEntity> getAll() {
        return waterServiceRepository.findAll();
    }

    public WaterServiceResponse getOne(Long id) {
        Optional<WaterServiceEntity> waterServiceOptional = waterServiceRepository.findById(id);
        return waterServiceOptional.map(service -> {
                    WaterServiceResponse response = electricityServiceMapper.toWaterServiceResponse(service);
                    response.setPrices(tieredPricingService.getAllByServiceId(service.getId()));
                    return response;
                }
        ).orElse(null);
    }

}
