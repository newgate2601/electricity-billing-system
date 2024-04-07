package com.example.electricitybillingsystem.vo.response;

import com.example.electricitybillingsystem.model.TieredPricingEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WaterServiceResponse {

    private Long id;

    private String name;

    private String description;

    private List<TieredPricingEntity> prices;

}
