package com.example.electricitybillingsystem.service;

import com.example.electricitybillingsystem.model.TaxEntity;
import com.example.electricitybillingsystem.repository.TaxRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaxService {
    private final TaxRepo taxRepo;

    public List<TaxEntity> getAll() {
        return taxRepo.findAll();
    }

    public TaxEntity getOne(Long id) {
        return taxRepo.findById(id).orElse(null);
    }

    public TaxEntity save(TaxEntity taxEntity) {
        return taxRepo.save(taxEntity);
    }
}
