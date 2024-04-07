package com.example.electricitybillingsystem.vo.request;

import com.example.electricitybillingsystem.model.TieredPricingEntity;
import lombok.Data;

import java.util.List;

@Data
public class UpdatePriceRequest {

    private List<TieredPricingEntity> currentPrices;

    private List<TieredPricingEntity> newPrices;
}
