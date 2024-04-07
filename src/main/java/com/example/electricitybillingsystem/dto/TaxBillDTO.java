package com.example.electricitybillingsystem.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class TaxBillDTO {
    private String name;
    private float price;
}
