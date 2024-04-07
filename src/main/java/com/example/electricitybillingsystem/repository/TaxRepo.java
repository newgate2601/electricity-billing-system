package com.example.electricitybillingsystem.repository;

import com.example.electricitybillingsystem.model.TaxEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaxRepo extends JpaRepository<TaxEntity, Long> {
    List<TaxEntity> findByIsStatus(Boolean status);

    @Modifying
    @Query("UPDATE TaxEntity e SET e.isStatus = false")
    void updateAllStatusToFalse();
}
