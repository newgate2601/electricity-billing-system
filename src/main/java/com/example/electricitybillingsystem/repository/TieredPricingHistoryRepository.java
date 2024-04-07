package com.example.electricitybillingsystem.repository;

import com.example.electricitybillingsystem.model.TieredPricingHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TieredPricingHistoryRepository extends JpaRepository<TieredPricingHistory, Long> {
    List<TieredPricingHistory> findAllByIdIn(List<Long> ids);


}
