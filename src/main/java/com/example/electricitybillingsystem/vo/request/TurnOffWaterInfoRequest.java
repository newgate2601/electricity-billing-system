package com.example.electricitybillingsystem.vo.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TurnOffWaterInfoRequest {
    private String ward,startTime,endTime;
}
