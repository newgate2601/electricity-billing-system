package com.example.electricitybillingsystem.mapper;

import com.example.electricitybillingsystem.model.WaterServiceEntity;
import com.example.electricitybillingsystem.vo.response.WaterServiceResponse;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-04-21T20:19:12+0700",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.38.0.v20240325-1403, environment: Java 17.0.10 (Eclipse Adoptium)"
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
