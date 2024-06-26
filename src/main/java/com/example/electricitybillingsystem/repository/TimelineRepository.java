package com.example.electricitybillingsystem.repository;

import com.example.electricitybillingsystem.model.TimelineEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface TimelineRepository extends JpaRepository<TimelineEntity, Long> {
    List<TimelineEntity> findAllByApartmentId(Long apartmentId);

}
