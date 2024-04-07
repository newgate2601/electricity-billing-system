package com.example.electricitybillingsystem.dto;

import com.example.electricitybillingsystem.model.AddressEntity;
import com.example.electricitybillingsystem.model.CustomerEntity;
import com.example.electricitybillingsystem.model.TimelineEntity;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class DepartmentDTO {
    private Long id;
    private String des;
    private AddressEntity addressEntity;
    private CustomerEntity customerEntity;
    private TimelineEntity timelineEntities;
}
