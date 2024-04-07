package com.example.electricitybillingsystem.service;

import com.example.electricitybillingsystem.common.Common;
import com.example.electricitybillingsystem.vo.dto.BillBeforePaymentResponse;
import com.example.electricitybillingsystem.vo.dto.BillAfterPaymentResponse;
import com.example.electricitybillingsystem.vo.dto.ApartmentDTO;
import com.example.electricitybillingsystem.vo.dto.TaxBillDTO;
import com.example.electricitybillingsystem.mapper.BillMapper;
import com.example.electricitybillingsystem.model.*;
import com.example.electricitybillingsystem.repository.*;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class BillService {
    private final BillMapper billMapper;

    private final BillRepository billRepository;
    private final ApartmentRepository apartmentRepository;
    private final AddressRepository addressRepository;
    private final CustomerRepository customerRepository;
    private final TimelineRepo timelineRepo;
    private final TaxBillRepo taxBillRepo;
    private final TaxRepo taxRepo;


    public Page<BillBeforePaymentResponse> getAllBillBeforePayment(Integer year, Integer month, Pageable pageable) {
        Page<BillEntity> billEntities = billRepository.findAllByStatus(false, pageable);
        List<Long> departmentIds = billEntities.stream().map(BillEntity::getApartmentId).collect(Collectors.toList());
        List<ApartmentEntity> departmentEntities = apartmentRepository.findAllByIdIn(departmentIds);

        Map<Long, AddressEntity> addressEntityMap = addressRepository.findAllByIdIn(
                departmentEntities.stream().map(ApartmentEntity::getAddressId).collect(Collectors.toList())
        ).stream().collect(Collectors.toMap(AddressEntity::getId, Function.identity()));

        Map<Long, CustomerEntity> customerEntityMap = customerRepository.findAllByIdIn(
                departmentEntities.stream().map(ApartmentEntity::getCustomerId).collect(Collectors.toList())
        ).stream().collect(Collectors.toMap(CustomerEntity::getId, Function.identity()));

        Map<Long, TimelineEntity> timelineEntityMap = timelineRepo.findAllByMonthAndDepartmentIn(year, month, departmentIds)
                .stream().collect(Collectors.toMap(TimelineEntity::getApartmentId, Function.identity()));

        List<ApartmentDTO> departmentDTOS = departmentEntities.stream().map(
                departmentEntity -> {
                    return ApartmentDTO.builder()
                            .id(departmentEntity.getId())
                            .addressEntity(addressEntityMap.get(departmentEntity.getAddressId()))
                            .des(departmentEntity.getDescription())
                            .customerEntity(customerEntityMap.get(departmentEntity.getCustomerId()))
                            .timelineEntities(timelineEntityMap.get(departmentEntity.getId()))
                            .build();
                }
        ).collect(Collectors.toList());

        Map<Long, ApartmentDTO> departmentDTOMap = departmentDTOS.stream().collect(Collectors.toMap(
                ApartmentDTO::getId, Function.identity()
        ));

        Map<Long, List<Long>> taxBillMap = taxBillRepo.findAllByBillIdIn(
                billEntities.stream().map(BillEntity::getId).collect(Collectors.toList())
        ).stream().collect(Collectors.groupingBy(TaxBillEntity::getBillId,
                Collectors.mapping(TaxBillEntity::getTaxId, Collectors.toList())));
        // taxBillMap bill_id = 1 ,  tax_id = 1,2
        return billEntities.map(
                billEntity -> {
                    BillBeforePaymentResponse billBeforePaymentResponse = billMapper.getResponseFromEntity(billEntity);
                    billBeforePaymentResponse.setApartment(departmentDTOMap.get(billEntity.getApartmentId()));
                    billBeforePaymentResponse.setTaxs(taxRepo.findAllByIdIn(taxBillMap.get(billEntity.getId())).stream()
                            .map(taxEntity ->  {
                                return TaxBillDTO.builder()
                                        .name(taxEntity.getName())
                                        .price(taxEntity.getTax())
                                        .build();
                            }).collect(Collectors.toList())
                    );
                    return billBeforePaymentResponse;
                }
        );
    }

    public Page<BillAfterPaymentResponse> getAllBillAfterPayment(Integer year, Integer month, Pageable pageable) {
        Page<BillEntity> billEntities = billRepository.findAllByStatus(true, pageable);
        List<Long> departmentIds = billEntities.stream().map(BillEntity::getApartmentId).collect(Collectors.toList());
        List<ApartmentEntity> departmentEntities = apartmentRepository.findAllByIdIn(departmentIds);

        Map<Long, AddressEntity> addressEntityMap = addressRepository.findAllByIdIn(
                departmentEntities.stream().map(ApartmentEntity::getAddressId).collect(Collectors.toList())
        ).stream().collect(Collectors.toMap(AddressEntity::getId, Function.identity()));

        Map<Long, CustomerEntity> customerEntityMap = customerRepository.findAllByIdIn(
                departmentEntities.stream().map(ApartmentEntity::getCustomerId).collect(Collectors.toList())
        ).stream().collect(Collectors.toMap(CustomerEntity::getId, Function.identity()));

        Map<Long, TimelineEntity> timelineEntityMap = timelineRepo.findAllByMonthAndDepartmentIn(year, month, departmentIds)
                .stream().collect(Collectors.toMap(TimelineEntity::getApartmentId, Function.identity()));

        List<ApartmentDTO> departmentDTOS = departmentEntities.stream().map(
                departmentEntity -> {
                    return ApartmentDTO.builder()
                            .id(departmentEntity.getId())
                            .addressEntity(addressEntityMap.get(departmentEntity.getAddressId()))
                            .des(departmentEntity.getDescription())
                            .customerEntity(customerEntityMap.get(departmentEntity.getCustomerId()))
                            .timelineEntities(timelineEntityMap.get(departmentEntity.getId()))
                            .build();
                }
        ).collect(Collectors.toList());

        Map<Long, ApartmentDTO> departmentDTOMap = departmentDTOS.stream().collect(Collectors.toMap(
                ApartmentDTO::getId, Function.identity()
        ));

        Map<Long, List<Long>> taxBillMap = taxBillRepo.findAllByBillIdIn(
                billEntities.stream().map(BillEntity::getId).collect(Collectors.toList())
        ).stream().collect(Collectors.groupingBy(TaxBillEntity::getBillId,
                Collectors.mapping(TaxBillEntity::getTaxId, Collectors.toList())));
        return billEntities.map(
                billEntity -> {
                    BillAfterPaymentResponse billAfterPaymentResponse = billMapper.getResponseAfterFromEntity(billEntity);
                    billAfterPaymentResponse.setDepartment(departmentDTOMap.get(billEntity.getApartmentId()));
                    billAfterPaymentResponse.setTaxs(taxRepo.findAllByIdIn(taxBillMap.get(billEntity.getId())).stream()
                            .map(taxEntity ->  {
                                return TaxBillDTO.builder()
                                        .name(taxEntity.getName())
                                        .price(taxEntity.getTax())
                                        .build();
                            }).collect(Collectors.toList())
                    );
                    return billAfterPaymentResponse;
                }
        );
    }

}
