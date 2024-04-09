package com.example.electricitybillingsystem.service;

import com.example.electricitybillingsystem.model.ApartmentEntity;
import com.example.electricitybillingsystem.repository.ApartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ApartmentService {
    private final ApartmentRepository apartmentRepository;

    public List<ApartmentEntity> getAllApartmentByIds(List<Long> apartmentIds) {
        return apartmentRepository.findAllByIdIn(apartmentIds);
    }
}
