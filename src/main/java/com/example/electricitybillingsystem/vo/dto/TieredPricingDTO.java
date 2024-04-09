package com.example.electricitybillingsystem.vo.dto;

import lombok.*;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class TieredPricingDTO {
    private BigDecimal price;
    private String name;
    private Long startNumber;
    private Long endNumber;
    private String serviceName;
}
