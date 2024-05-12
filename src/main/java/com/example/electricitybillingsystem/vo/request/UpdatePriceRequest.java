package com.example.electricitybillingsystem.vo.request;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class UpdatePriceRequest {

    private Long electricityServiceId;

    private List<Price> newPrices;

    @Data
    @Builder
    public static class Price {

        private Long startNumber;

        private Long endNumber;

        private BigDecimal value;

    }

}
