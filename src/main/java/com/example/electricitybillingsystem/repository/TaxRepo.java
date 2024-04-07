package com.example.electricitybillingsystem.repository;

import com.example.electricitybillingsystem.model.TaxEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface TaxRepo extends JpaRepository<TaxEntity,Long> {
    List<TaxEntity> findAllByIdIn(Collection<Long> taxIds);
}
