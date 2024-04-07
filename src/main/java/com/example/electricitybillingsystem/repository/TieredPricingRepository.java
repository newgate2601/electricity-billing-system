package com.example.electricitybillingsystem.repository;

import com.example.electricitybillingsystem.model.TieredPricing;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TieredPricingRepository extends JpaRepository<TieredPricing, Long> {

    List<TieredPricing> findAllByServiceId(Long serviceId);

}
