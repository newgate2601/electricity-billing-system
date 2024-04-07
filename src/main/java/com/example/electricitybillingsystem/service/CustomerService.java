package com.example.electricitybillingsystem.service;

import com.example.electricitybillingsystem.model.CustomerEntity;
import com.example.electricitybillingsystem.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;

    public List<CustomerEntity> getAllCustomerByIds(List<Long> customerIds) {
        return customerRepository.findAllByIdIn(customerIds);
    }

    public List<CustomerEntity> getAllCustomer(){
        return customerRepository.findAll();
    }
}
