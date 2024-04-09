package com.example.electricitybillingsystem.service;

import com.example.electricitybillingsystem.model.BillEntity;
import com.example.electricitybillingsystem.model.CustomerEntity;
import com.example.electricitybillingsystem.repository.BillRepository;
import com.example.electricitybillingsystem.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final BillRepository billRepository;

    public List<CustomerEntity> getAllCustomerByIds(List<Long> customerIds) {
        return customerRepository.findAllByIdIn(customerIds);
    }
    public List<CustomerEntity> getAllCustomerByBillIds(List<Long> billIds){
            List<Long> customerIds =  billRepository.findAllByIdIn(billIds).stream().map(
                BillEntity::getCustomerId
        ).collect(Collectors.toList());
            return customerRepository.findAllByIdIn(customerIds);
    }
    public Map<Long, BillEntity> getBillMap(List<Long> billIds){
        return billRepository.findAllByIdIn(billIds).stream().collect(Collectors.toMap(
                BillEntity::getCustomerId, Function.identity()
        ));
    }
    public List<CustomerEntity> getAllCustomer(){
        return customerRepository.findAll();
    }
}
