package com.example.electricitybillingsystem.mapper;

import com.example.electricitybillingsystem.model.BillEntity;
import com.example.electricitybillingsystem.vo.dto.BillResponse;
import com.example.electricitybillingsystem.vo.dto.BillAfterPaymentResponse;
import com.example.electricitybillingsystem.vo.dto.BillBeforePaymentResponse;
import org.mapstruct.Mapper;

@Mapper
public interface BillMapper {
    BillBeforePaymentResponse getResponseFromEntity(BillEntity billEntity);
    BillAfterPaymentResponse getResponseAfterFromEntity(BillEntity billEntity);
    BillResponse getBillResponseBy(BillEntity billEntity);
}
