package com.example.electricitybillingsystem.vo.request;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class UpdateTaxRequest {

    private Long id;

    private BigDecimal tax;

}
