package com.example.electricitybillingsystem.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity
@Table(name = "tbl_electricity_service")
public class ElectricityServiceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private float price;
    private String name;
    private String des;
    private Long departmentId;
}
