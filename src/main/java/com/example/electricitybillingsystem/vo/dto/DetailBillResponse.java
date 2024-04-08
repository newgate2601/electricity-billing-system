package com.example.electricitybillingsystem.vo.dto;

import lombok.*;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class DetailBillResponse {
    private Long useWaterNumner;
    private BigDecimal piceWater;
    private BigDecimal cost;
    private String taxName;
    private BigDecimal priceTax;
}
