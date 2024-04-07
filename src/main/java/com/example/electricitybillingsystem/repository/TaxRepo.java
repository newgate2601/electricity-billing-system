package com.example.electricitybillingsystem.repository;

import com.example.electricitybillingsystem.model.TaxEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaxRepo extends JpaRepository<TaxEntity,Long> {
}
