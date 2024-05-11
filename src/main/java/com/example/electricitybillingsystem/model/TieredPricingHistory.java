package com.example.electricitybillingsystem.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tbl_tiered_pricing_history")
public class TieredPricingHistory extends BaseEnt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal price;
    private String tieredPricingName;
    private Long startNumber;
    private Long endNumber;
    private String waterServiceName;
    private String waterServiceDes;
}
