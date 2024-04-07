package com.example.electricitybillingsystem.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@Entity
@Table(name = "tbl_timeline_entity")
public class TimelineEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long startNumber;
    private Long endNumber;
    private OffsetDateTime submitTime;
    private Long departmentId;
}
