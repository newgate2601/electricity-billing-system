package com.example.electricitybillingsystem.vo.dto;

import lombok.*;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class TaxBillDTO {
    private String name;
    private BigDecimal price;
}
