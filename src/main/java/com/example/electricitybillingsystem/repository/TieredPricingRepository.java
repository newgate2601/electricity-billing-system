package com.example.electricitybillingsystem.repository;

import com.example.electricitybillingsystem.model.TieredPricingEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TieredPricingRepository extends JpaRepository<TieredPricingEntity, Long> {

    List<TieredPricingEntity> findAllByElectricityServiceId(Long serviceId);

    void deleteTieredPricingEntitiesByIdIn(List<Long> ids);

}
