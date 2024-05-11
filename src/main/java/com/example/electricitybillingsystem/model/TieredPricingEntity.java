package com.example.electricitybillingsystem.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@Entity
@EqualsAndHashCode(callSuper = true)
@Table(name = "tbl_tiered_pricing")
@SuperBuilder
public class TieredPricingEntity extends BaseEnt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "_value")
    private BigDecimal value;

    private Long startNumber;

    private Long endNumber;

    private Boolean status;

    private Long electricityServiceId;
    private Boolean isStatus;

}
