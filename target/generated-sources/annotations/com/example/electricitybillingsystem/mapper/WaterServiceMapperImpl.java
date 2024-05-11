package com.example.electricitybillingsystem.mapper;

import com.example.electricitybillingsystem.model.WaterServiceEntity;
import com.example.electricitybillingsystem.vo.response.WaterServiceResponse;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-05-10T23:53:27+0700",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.9 (Amazon.com Inc.)"
)
@Component
public class WaterServiceMapperImpl implements WaterServiceMapper {

    @Override
    public WaterServiceResponse toWaterServiceResponse(WaterServiceEntity waterService) {
        if ( waterService == null ) {
            return null;
        }

        WaterServiceResponse waterServiceResponse = new WaterServiceResponse();

        waterServiceResponse.setId( waterService.getId() );
        waterServiceResponse.setName( waterService.getName() );

        return waterServiceResponse;
    }
}
