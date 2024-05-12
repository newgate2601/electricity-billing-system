package com.example.electricitybillingsystem.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tbl_bill")
@Builder
public class BillEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private OffsetDateTime submitTime;
    private OffsetDateTime limitedTime;
    private String description;
    private String billCode;
    private BigDecimal price;
    private Boolean status;
    private String statusValue;
    private Long startNumber;
    private Long endNumber;
    private Long employeeId;
    private Long customerId;
    private Long apartmentId;
    @Column(name = "_month")
    private Integer month;
    @Column(name = "_year")
    private Integer year;
}
