package com.example.electricitybillingsystem.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.OffsetDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tbl_bill")
public class BillEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private OffsetDateTime submitTime;
    private String description;
    private String homeCode;
    private Double price;
    private String status;
    private Long startNumber;
    private Long endNumber;
    private Double taxVat;
    private Double taxBvmt;
    private Long employeeId;
    private Long departmentId;
    private Long customerId;
}
