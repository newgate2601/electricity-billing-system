package com.example.electricitybillingsystem.service;

import com.example.electricitybillingsystem.model.TaxBillEntity;
import com.example.electricitybillingsystem.model.TaxEntity;
import com.example.electricitybillingsystem.repository.TaxBillRepository;
import com.example.electricitybillingsystem.repository.TaxRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaxService {
    private final TaxRepository taxRepo;

    public List<TaxEntity> getAll() {
        return taxRepo.findAll();
    }

    public TaxEntity getOne(Long id) {
        return taxRepo.findById(id).orElse(null);
    }

    public TaxEntity save(Long id, BigDecimal tax) {
        TaxEntity entity = taxRepo.findById(id).orElseThrow();
        if (tax.compareTo(BigDecimal.valueOf(0)) <= 0 || tax.compareTo(BigDecimal.valueOf(100)) >= 0) {
            throw new IllegalArgumentException("Giá trị thuế phải lớn hơn 0 và nhỏ hơn 100");
        }
        entity.setTax(tax);
        return taxRepo.save(entity);
    }

    private final TaxRepository taxRepository;
    private final TaxBillRepository taxBillRepository;

    @Transactional(readOnly = true)
    public List<TaxEntity> getAllByStatus(Boolean status) {
        return taxRepository.findByIsStatus(status);
    }

    public List<TaxEntity> getAllTaxByBillId(Long id) {
        List<TaxBillEntity> taxBillEntities = taxBillRepository.findAllByBillId(id);
        if (taxBillEntities == null || taxBillEntities.size() == 0) {
            return null;
        }

        List<Long> taxIds = taxBillEntities.stream().map(TaxBillEntity::getTaxId).collect(Collectors.toList());
        if (taxIds == null || taxIds.size() == 0) {
            return null;
        }

        List<TaxEntity> taxEntities = taxRepository.findAllByIdIn(taxIds);

        return taxEntities;

    }

    @Transactional
    public void updateAllStatusToFalse() {
        taxRepository.updateAllStatusToFalse();
    }


}
