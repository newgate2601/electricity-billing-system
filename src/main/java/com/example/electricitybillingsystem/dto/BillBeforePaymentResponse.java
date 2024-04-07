package com.example.electricitybillingsystem.dto;

import com.example.electricitybillingsystem.entity.CustomerEntity;
import com.example.electricitybillingsystem.entity.TaxEntity;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class BillBeforePaymentResponse {
    private OffsetDateTime submitTime;
    private String description;
    private String billCode;
    private Double price;
    private Boolean status;
    private Long startNumber;
    private Long endNumber;
    private List<TaxBillDTO> taxs;
    private DepartmentDTO department;
}
