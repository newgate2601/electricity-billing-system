package com.example.electricitybillingsystem.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity
@Table(name = "tbl_tiered_pricing")
public class TieredPricingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Float price;
    private String name;
    private Long startNumber;
    private Long endNumber;
    private Boolean status;
    private Long electricityServiceId;

}
