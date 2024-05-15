package com.example.electricitybillingsystem.service;

import com.example.electricitybillingsystem.mapper.TieredPricingMapper;
import com.example.electricitybillingsystem.model.TaxEntity;
import com.example.electricitybillingsystem.model.TieredPricingEntity;
import com.example.electricitybillingsystem.model.WaterServiceEntity;
import com.example.electricitybillingsystem.repository.TieredPricingRepository;
import com.example.electricitybillingsystem.repository.WaterServiceRepository;
import com.example.electricitybillingsystem.vo.dto.TieredPricingDTO;
import com.example.electricitybillingsystem.vo.request.UpdatePriceRequest;
import com.example.electricitybillingsystem.vo.response.AdjustPricingResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TieredPricingService {
    private final TieredPricingRepository tieredPricingRepository;
    private final TieredPricingMapper tieredPricingMapper;
    private final TaxService taxService;
    private final WaterServiceRepository waterServiceRepository;

    public List<TieredPricingEntity> getAllByServiceId(Long id) {
        return tieredPricingRepository.findAllByElectricityServiceIdOrderByStartNumber(id);
    }

    @Transactional
    public Object updatePrice(UpdatePriceRequest request) {
        tieredPricingRepository.deleteAllByElectricityServiceId(request.getElectricityServiceId());
        tieredPricingRepository.saveAll(request.getNewPrices().stream().map(price -> {
            if (price.getStartNumber() < 0) {
                throw new IllegalArgumentException("Giá trị bắt đầu không được âm");
            }
            if (price.getEndNumber() < 0) {
                throw new IllegalArgumentException("Giá trị kết thúc không được âm");
            }
            if (price.getValue().compareTo(BigDecimal.ZERO) < 0) {
                throw new IllegalArgumentException("Giá tiền không được âm");
            }
            if (price.getStartNumber() >= price.getEndNumber()) {
                throw new IllegalArgumentException("Giá trị bắt đầu phải nhỏ hơn giá trị kết thúc");
            }

            TieredPricingEntity entity = tieredPricingMapper.toEntity(price);
            entity.setElectricityServiceId(request.getElectricityServiceId());
            entity.setIsStatus(true);
            entity.setStatus(true);
            return entity;
        }).toList());
        return true;
    }

    @Transactional(readOnly = true)
    public AdjustPricingResponse getAllByStatus() {
        List<TieredPricingEntity> tieredPricingEntities = tieredPricingRepository.findAllByIsStatus(true);
        List<TaxEntity> taxEntities = taxService.getAllByStatus(true);
        AdjustPricingResponse response = new AdjustPricingResponse();

        if (tieredPricingEntities == null && taxEntities == null) {
            return null;
        }
        List<TieredPricingDTO> tieredPricingDTOS = new ArrayList<>();
        if (tieredPricingEntities != null) {
            tieredPricingEntities.stream().forEach(tieredPricingEntity -> {
                Optional<WaterServiceEntity> waterServiceEntity = waterServiceRepository.findById(tieredPricingEntity.getElectricityServiceId());
                TieredPricingDTO tieredPricingDTO = tieredPricingMapper.toTieredPricingDTO(tieredPricingEntity);
                if (waterServiceEntity.isPresent()) {
                    tieredPricingDTO.setServiceName(waterServiceEntity.get().getName());
                } else {
                    tieredPricingDTO.setServiceName("NOT WATER SERVICE");
                }
                tieredPricingDTOS.add(tieredPricingDTO);
            });
            response.setTieredPricing(tieredPricingDTOS);
        }

        if (taxEntities != null) {
            response.setTaxs(taxEntities);
        }

        return response;
    }

    @Transactional
    public void updateAllStatusToFalse() {
        tieredPricingRepository.updateAllStatusToFalse();
    }
}
