package com.example.electricitybillingsystem.vo.dto;

import lombok.*;

import java.time.OffsetDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class BillBeforePaymentResponse {
    private Long id;
    private OffsetDateTime submitTime;
    private OffsetDateTime limitedTime;
    private String description;
    private String billCode;
    private Double price;
    private Boolean status;
    private Long startNumber;
    private Long endNumber;
    private List<TaxBillDTO> taxs;
    private ApartmentDTO apartment;
}
