package com.example.electricitybillingsystem.model;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "tbl_address")
@Builder
public class AddressEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String homeNumber;
    private String streetName;
    private String ward;
    private String district;
    private String city;
    private Long apartmentId;
}
