package com.example.electricitybillingsystem.repository;

import com.example.electricitybillingsystem.model.WaterService;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WaterServiceRepository extends JpaRepository<WaterService, Long> {
}
