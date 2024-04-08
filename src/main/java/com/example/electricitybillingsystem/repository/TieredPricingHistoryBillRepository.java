package com.example.electricitybillingsystem.repository;

import com.example.electricitybillingsystem.model.TieredPricingHistoryBill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TieredPricingHistoryBillRepository extends JpaRepository<TieredPricingHistoryBill, Long> {
    List<TieredPricingHistoryBill> findAllByBillId(Long id);
}
