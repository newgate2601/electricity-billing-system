package com.example.electricitybillingsystem.repository;

import com.example.electricitybillingsystem.model.WaterServiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WaterServiceRepository extends JpaRepository<WaterServiceEntity, Long> {
}
