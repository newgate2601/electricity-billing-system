package com.example.electricitybillingsystem.vo.response;

import com.example.electricitybillingsystem.model.TaxEntity;
import com.example.electricitybillingsystem.model.TieredPricingEntity;
import com.example.electricitybillingsystem.vo.dto.TieredPricingDTO;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class AdjustPricingResponse {
    List<TaxEntity> taxs;
    List<TieredPricingDTO> tieredPricing;
}
