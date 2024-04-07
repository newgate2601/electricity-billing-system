package com.example.electricitybillingsystem.mapper;

import com.example.electricitybillingsystem.dto.BillAfterPaymentResponse;
import com.example.electricitybillingsystem.dto.BillBeforePaymentResponse;
import com.example.electricitybillingsystem.model.BillEntity;
import org.mapstruct.Mapper;

@Mapper
public interface BillMapper {

    BillBeforePaymentResponse getResponseFromEntity(BillEntity billEntity);
    BillAfterPaymentResponse getResponseAfterFromEntity(BillEntity billEntity);
}
