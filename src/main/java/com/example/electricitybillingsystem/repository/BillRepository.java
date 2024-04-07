package com.example.electricitybillingsystem.repository;

import com.example.electricitybillingsystem.entity.BillEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BillRepository extends JpaRepository<BillEntity, Long> {

    Page<BillEntity> findAllByStatus(Boolean status, Pageable pageable);
}
