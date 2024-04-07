package com.example.electricitybillingsystem.repository;

import com.example.electricitybillingsystem.model.TimelineEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TimelineRepo extends JpaRepository<TimelineEntity, Long> {
    List<TimelineEntity> findAllByApartmentId(Long apartmentId);
}
