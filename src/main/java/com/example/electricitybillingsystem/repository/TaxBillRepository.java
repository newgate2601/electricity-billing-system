package com.example.electricitybillingsystem.repository;

import com.example.electricitybillingsystem.model.TaxBillEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaxBillRepository extends JpaRepository<TaxBillEntity, Long> {
    List<TaxBillEntity> findAllByBillId(Long id);
}
