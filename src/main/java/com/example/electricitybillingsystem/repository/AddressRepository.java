package com.example.electricitybillingsystem.repository;

import com.example.electricitybillingsystem.model.AddressEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface AddressRepository extends JpaRepository<AddressEntity, Long> {
    List<AddressEntity> findAllByWard(String ward);
    List<AddressEntity> findAllByIdIn(Collection<Long> addressIds);
}
