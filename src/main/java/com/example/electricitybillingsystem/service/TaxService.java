package com.example.electricitybillingsystem.service;

import com.example.electricitybillingsystem.model.TaxEntity;
import com.example.electricitybillingsystem.repository.TaxRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaxService {
    private final TaxRepo taxRepo;

    public TaxEntity save(TaxEntity taxEntity) {
        return taxRepo.save(taxEntity);
    }
}
