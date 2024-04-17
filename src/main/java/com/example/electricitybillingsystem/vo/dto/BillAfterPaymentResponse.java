package com.example.electricitybillingsystem.vo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BillAfterPaymentResponse {
    private Long id;
    private String submitTimeResponse;
    private String limitedTimeResponse;
    private String description;
    private String billCode;
    private Double price;
    private Boolean status;
    private Long startNumber;
    private Long endNumber;
    private List<TaxBillDTO> taxs;
    private ApartmentDTO apartment;
}
