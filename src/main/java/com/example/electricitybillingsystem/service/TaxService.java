package com.example.electricitybillingsystem.service;

import com.example.electricitybillingsystem.model.TaxEntity;
import com.example.electricitybillingsystem.repository.TaxRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaxService {
    private final TaxRepository taxRepository;

    @Transactional(readOnly = true)
    public List<TaxEntity> getAllByStatus(Boolean status) {
        return taxRepository.findByIsStatus(status);
    }

    @Transactional
    public void updateAllStatusToFalse(){
        taxRepository.updateAllStatusToFalse();
    }


}
