package com.example.electricitybillingsystem.dto;

import com.example.electricitybillingsystem.entity.AddressEntity;
import com.example.electricitybillingsystem.entity.CustomerEntity;
import com.example.electricitybillingsystem.entity.TimelineEntity;
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
    private List<TimelineEntity> timelineEntities;
}
