package com.example.electricitybillingsystem.service;

import com.example.electricitybillingsystem.common.Common;
import com.example.electricitybillingsystem.dto.BillAfterPaymentResponse;
import com.example.electricitybillingsystem.dto.BillBeforePaymentResponse;
import com.example.electricitybillingsystem.dto.DepartmentDTO;
import com.example.electricitybillingsystem.dto.TaxBillDTO;
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
    private final DepartmentRepository departmentRepository;
    private final AddressRepository addressRepository;
    private final CustomerRepository customerRepository;
    private final TimelineRepo timelineRepo;
    private final TaxBillRepo taxBillRepo;
    private final TaxRepo taxRepo;


//    @Transactional(readOnly = true)
//    public Page<BillBeforePaymentResponse> getAllBillBeforePayment(Pageable pageable) {
//        Page<BillEntity> billEntities = billRepository.findAllByStatus(false, pageable);
//        return billEntities.map(
//                billEntity -> {
//                    DepartmentEntity departmentEntity = departmentRepository.findById(billEntity.getDepartmentId())
//                            .orElseThrow(() -> new RuntimeException(Common.NOT_FOUND_RECORD));
//
//                    AddressEntity addressEntity = addressRepository.findById(departmentEntity.getAddressId())
//                            .orElseThrow(() -> new RuntimeException(Common.NOT_FOUND_RECORD));
//                    CustomerEntity customerEntity = customerRepository.findById(departmentEntity.getCustomerId())
//                            .orElseThrow(() -> new RuntimeException(Common.NOT_FOUND_RECORD));
//                    List<TimelineEntity> timelineEntities = timelineRepo.findAllByDepartmentId(departmentEntity.getId());
//
//                    List<TaxBillEntity> taxBillEntities = taxBillRepo.findAllByBillId(billEntity.getId());
//                    List<TaxBillDTO> taxBillDTOS = new ArrayList<>();
//
//                    taxBillEntities.forEach(taxBillEntity -> {
//                        Optional<TaxEntity> taxEntity = taxRepo.findById(taxBillEntity.getTaxId());
//                        String taxName = taxEntity.get().getName();
//
//                        TaxBillDTO taxBillDTO = TaxBillDTO.builder()
//                                .name(taxName)
//                                .price(taxBillEntity.getPrice())
//                                .build();
//                        taxBillDTOS.add(taxBillDTO);
//                    });
//
//                    DepartmentDTO departmentDTO = DepartmentDTO.builder()
//                            .id(departmentEntity.getId())
//                            .des(departmentEntity.getDescription())
//                            .addressEntity(addressEntity)
//                            .customerEntity(customerEntity)
//                            .timelineEntities(timelineEntities).build();
//
//
//                    BillBeforePaymentResponse billBeforePaymentResponse = billMapper.getResponseFromEntity(billEntity);
//                    billBeforePaymentResponse
//                            .setTaxs(taxBillDTOS);
//                    billBeforePaymentResponse
//                            .setDepartment(departmentDTO);
//
//                    return billBeforePaymentResponse;
//                }
//        );
//    }

    public Page<BillAfterPaymentResponse> getAllBillAfterPayment(Integer month, Integer year, Pageable pageable) {
        Page<BillEntity> billEntities = billRepository.findAllByStatus(true, pageable);
        List<Long> departmentIds = billEntities.stream().map(BillEntity::getDepartmentId).collect(Collectors.toList());
        List<DepartmentEntity> departmentEntities = departmentRepository.findAllByIdIn(departmentIds);
        Map<Long, DepartmentEntity> departmentEntityMap = departmentEntities.stream()
                .collect(Collectors.toMap(DepartmentEntity::getId, Function.identity()));

        Map<Long, AddressEntity> addressEntityMap = addressRepository.findAllByIdIn(
                departmentEntities.stream().map(DepartmentEntity::getAddressId).collect(Collectors.toList())
        ).stream().collect(Collectors.toMap(AddressEntity::getId, Function.identity()));

        Map<Long, CustomerEntity> customerEntityMap = customerRepository.findAllByIdIn(
                departmentEntities.stream().map(DepartmentEntity::getCustomerId).collect(Collectors.toList())
        ).stream().collect(Collectors.toMap(CustomerEntity::getId, Function.identity()));

        Map<Long, TimelineEntity> timelineEntityMap = timelineRepo.findByMonthAndDepartmentIn(year, month, departmentIds)
                .stream().collect(Collectors.toMap(TimelineEntity::getDepartmentId, Function.identity()));

        List<DepartmentDTO> departmentDTOS = departmentEntities.stream().map(
                departmentEntity -> {
                    return DepartmentDTO.builder()
                            .id(departmentEntity.getId())
                            .addressEntity(addressEntityMap.get(departmentEntity.getAddressId()))
                            .des(departmentEntity.getDescription())
                            .customerEntity(customerEntityMap.get(departmentEntity.getCustomerId()))
                            .timelineEntities(timelineEntityMap.get(departmentEntity.getId()))
                            .build();
                }
        ).collect(Collectors.toList());

        Map<Long, DepartmentDTO> departmentDTOMap = departmentDTOS.stream().collect(Collectors.toMap(
                DepartmentDTO::getId, Function.identity()
        ));

        Map<Long, List<Long>> taxBillMap = taxBillRepo.findAllByBillIdIn(
                billEntities.stream().map(BillEntity::getId).collect(Collectors.toList())
        ).stream().collect(Collectors.groupingBy(TaxBillEntity::getBillId,
                Collectors.mapping(TaxBillEntity::getTaxId, Collectors.toList())));
        return billEntities.map(
                billEntity -> {
                    BillAfterPaymentResponse billAfterPaymentResponse = billMapper.getResponseAfterFromEntity(billEntity);
                    billAfterPaymentResponse.setDepartment(departmentDTOMap.get(billEntity.getDepartmentId()));
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
