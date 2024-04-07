package com.example.electricitybillingsystem.mapper;

import com.example.electricitybillingsystem.model.WaterServiceEntity;
import com.example.electricitybillingsystem.vo.response.WaterServiceResponse;
import org.mapstruct.Mapper;

@Mapper
public interface WaterServiceMapper {

    WaterServiceResponse toWaterServiceResponse(WaterServiceEntity waterService);
}
