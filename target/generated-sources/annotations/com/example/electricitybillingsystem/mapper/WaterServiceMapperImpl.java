package com.example.electricitybillingsystem.mapper;

import com.example.electricitybillingsystem.model.WaterServiceEntity;
import com.example.electricitybillingsystem.vo.response.WaterServiceResponse;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-04-07T21:45:57+0700",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 18.0.2 (Amazon.com Inc.)"
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
