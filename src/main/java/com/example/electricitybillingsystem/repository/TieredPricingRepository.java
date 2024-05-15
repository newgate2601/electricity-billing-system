package com.example.electricitybillingsystem.repository;

import com.example.electricitybillingsystem.model.TieredPricingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TieredPricingRepository extends JpaRepository<TieredPricingEntity, Long> {

    List<TieredPricingEntity> findAllById(Long serviceId);
    List<TieredPricingEntity> findAllByIsStatus(Boolean status);
    List<TieredPricingEntity> findAllByElectricityServiceIdOrderByStartNumber(Long serviceId);

    void deleteAllByElectricityServiceId(Long id);

    @Modifying
    @Query("UPDATE TieredPricingEntity e SET e.isStatus = false")
    void updateAllStatusToFalse();
}
