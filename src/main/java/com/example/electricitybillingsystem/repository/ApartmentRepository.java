package com.example.electricitybillingsystem.repository;

import com.example.electricitybillingsystem.model.ApartmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApartmentRepository extends JpaRepository<ApartmentEntity, Long> {

    List<ApartmentEntity> findAllByIdIn(List<Long> apartmentIds);
}
