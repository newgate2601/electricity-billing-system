package com.example.electricitybillingsystem.mapper;

import com.example.electricitybillingsystem.model.TieredPricingEntity;
import com.example.electricitybillingsystem.vo.dto.TieredPricingDTO;
import com.example.electricitybillingsystem.vo.request.UpdatePriceRequest;
import org.mapstruct.Mapper;

@Mapper
public interface TieredPricingMapper {

    TieredPricingDTO toTieredPricingDTO(TieredPricingEntity tieredPricingEntity);

    TieredPricingEntity toEntity(UpdatePriceRequest.Price price);

}
