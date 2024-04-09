package com.example.electricitybillingsystem.repository;

import com.example.electricitybillingsystem.model.BillEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface BillRepository extends JpaRepository<BillEntity, Long> {

    Page<BillEntity> findAllByStatus(Boolean status, Pageable pageable);
    List<BillEntity> findAllByIdIn(Collection<Long> billIds);

//    @Query ("SELECT i FROM BillEntity i WHERE i.status = false AND EXTRACT(DAY FROM (CURRENT_TIMESTAMP - i.submitTime)) >= 7")
//        Page<BillEntity> findBillOverLimitedTime(Pageable pageable);
}
