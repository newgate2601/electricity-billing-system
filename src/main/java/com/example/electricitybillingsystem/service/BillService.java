package com.example.electricitybillingsystem.service;

import com.example.electricitybillingsystem.dto.BillBeforePaymentResponse;
import com.example.electricitybillingsystem.entity.AddressEntity;
import com.example.electricitybillingsystem.entity.BillEntity;
import com.example.electricitybillingsystem.entity.CustomerEntity;
import com.example.electricitybillingsystem.entity.DepartmentEntity;
import com.example.electricitybillingsystem.mapper.BillMapper;
import com.example.electricitybillingsystem.repository.AddressRepository;
import com.example.electricitybillingsystem.repository.BillRepository;
import com.example.electricitybillingsystem.repository.CustomerRepository;
import com.example.electricitybillingsystem.repository.DepartmentRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class BillService {
    private final BillRepository billRepository;
    private final BillMapper billMapper;
    private final DepartmentRepository departmentRepository;
    private final AddressRepository addressRepository;
    private final CustomerRepository customerRepository;

    @Transactional(readOnly = true)
    public Page<BillBeforePaymentResponse> getAllBillBeforePayment(Pageable pageable) {
        Page<BillEntity> billEntities = billRepository.findAllByStatus("NOT SUBMITED", pageable);
        return billEntities.map(
                billEntity -> {
                    DepartmentEntity departmentEntity = departmentRepository.findById(billEntity.getDepartmentId())
                            .orElseThrow(() -> new RuntimeException("NOT FOUND DEPARTMENT"));
                    AddressEntity addressEntity = addressRepository.findById(departmentEntity.getAddressId())
                            .orElseThrow(() -> new RuntimeException("NOT FOUND ADDRESS"));
                    CustomerEntity customerEntity = customerRepository.findById(billEntity.getCustomerId())
                            .orElseThrow(() -> new RuntimeException("NOT FOUND USER"));
                    BillBeforePaymentResponse billBeforePaymentResponse = billMapper.getResponseFromEntity(billEntity);
                    billBeforePaymentResponse.setAddress(addressEntity.getHomeNumber()
                    +" "+ addressEntity.getWard()+" "+addressEntity.getDistrict()+" "+addressEntity.getCity());
                    billBeforePaymentResponse.setUserNumber(
                            billEntity.getEndNumber() - billEntity.getStartNumber()
                    );
                    billBeforePaymentResponse.setNameCustomer(customerEntity.getName());
                    return billBeforePaymentResponse;
                }
        );
    }
}
