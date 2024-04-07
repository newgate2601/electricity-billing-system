package com.example.electricitybillingsystem.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class BillBeforePaymentResponse {
    private Long id;
    private String nameCustomer;
//    private String departmentName;
    private String homeCode;
    private String address;
    private Long startNumber;
    private Long endNumber;
    private Long userNumber;
    private Double price;
    private String status;
}
