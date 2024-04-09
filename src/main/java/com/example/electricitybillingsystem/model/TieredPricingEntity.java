package com.example.electricitybillingsystem.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@Entity
@EqualsAndHashCode(callSuper = true)
@Table(name = "tbl_tiered_pricing")
public class TieredPricingEntity extends BaseEnt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal value;

    private Long startNumber;

    private Long endNumber;

    private Boolean status;

    private Long electricityServiceId;
    private Boolean isStatus;

}
