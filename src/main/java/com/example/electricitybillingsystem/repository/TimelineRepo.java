package com.example.electricitybillingsystem.repository;

import com.example.electricitybillingsystem.model.TimelineEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface TimelineRepo extends JpaRepository<TimelineEntity, Long> {
    List<TimelineEntity> findAllByDepartmentId(Long departmentId);
    @Query("SELECT t FROM TimelineEntity t " +
            "WHERE FUNCTION('YEAR', t.submitTime) = :year " +
            "AND FUNCTION('MONTH', t.submitTime) = :month " +
            "AND t.departmentId = :departmentId")
    List<TimelineEntity> findByMonthAndDepartmentIn(Integer year, Integer month, Collection<Long> departmentId);
}
