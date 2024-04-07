package com.example.electricitybillingsystem.service;

import com.example.electricitybillingsystem.model.TaxEntity;
import com.example.electricitybillingsystem.repository.TaxRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaxService {
    private final TaxRepo taxRepo;

    @Transactional(readOnly = true)
    public List<TaxEntity> getAllByStatus(Boolean status) {
        return taxRepo.findByIsStatus(status);
    }

    @Transactional
    public void updateAllStatusToFalse(){
        taxRepo.updateAllStatusToFalse();
    }


}
