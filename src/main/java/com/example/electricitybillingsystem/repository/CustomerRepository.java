package com.example.electricitybillingsystem.repository;

import com.example.electricitybillingsystem.model.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<CustomerEntity, Long> {
    List<CustomerEntity> findAllByIdIn(Collection<Long> customerIds);
}
