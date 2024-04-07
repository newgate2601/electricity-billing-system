package com.example.electricitybillingsystem.service;

import com.example.electricitybillingsystem.model.AddressEntity;
import com.example.electricitybillingsystem.model.ApartmentEntity;
import com.example.electricitybillingsystem.model.CustomerEntity;
import com.example.electricitybillingsystem.repository.AddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AddressService {
    private final AddressRepository addressRepository;
    private final ApartmentService apartmentService;
    private final CustomerService customerService;

    public List<CustomerEntity> getAllCustomerByWard(String ward) {
        List<AddressEntity> addressEntities = addressRepository.findAllByWard(ward);
        if (addressEntities == null || addressEntities.size() == 0) {
            return null;
        }

        List<Long> apartmentIds = addressEntities.stream().map(AddressEntity::getApartmentId).collect(Collectors.toList());
        List<ApartmentEntity> apartmentEntities = apartmentService.getAllApartmentByIds(apartmentIds);
        if (apartmentEntities == null || apartmentEntities.size() == 0) {
            return null;
        }

        List<Long> customerIds = apartmentEntities.stream().map(ApartmentEntity::getCustomerId).collect(Collectors.toList());
        List<CustomerEntity> customers = customerService.getAllCustomerByIds(customerIds);
        return customers;
    }
}
