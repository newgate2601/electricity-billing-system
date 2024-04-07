package com.example.electricitybillingsystem.mapper;

import com.example.electricitybillingsystem.model.TieredPricingEntity;
import com.example.electricitybillingsystem.vo.dto.TieredPricingDTO;
import org.mapstruct.Mapper;

@Mapper
public interface TieredPricingMapper {
    TieredPricingDTO toTieredPricingDTO(TieredPricingEntity tieredPricingEntity);
}
