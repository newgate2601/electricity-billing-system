package com.example.electricitybillingsystem.service;

import com.example.electricitybillingsystem.common.Common;
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
import java.util.Optional;

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


    @Transactional(readOnly = true)
    public Page<BillBeforePaymentResponse> getAllBillBeforePayment(Pageable pageable) {
        Page<BillEntity> billEntities = billRepository.findAllByStatus(false, pageable);
        return billEntities.map(
                billEntity -> {
                    DepartmentEntity departmentEntity = departmentRepository.findById(billEntity.getDepartmentId())
                            .orElseThrow(() -> new RuntimeException(Common.NOT_FOUND_RECORD));

                    AddressEntity addressEntity = addressRepository.findById(departmentEntity.getAddressId())
                            .orElseThrow(() -> new RuntimeException(Common.NOT_FOUND_RECORD));
                    CustomerEntity customerEntity = customerRepository.findById(departmentEntity.getCustomerId())
                            .orElseThrow(() -> new RuntimeException(Common.NOT_FOUND_RECORD));
                    List<TimelineEntity> timelineEntities = timelineRepo.findAllByDepartmentId(departmentEntity.getId());

                    List<TaxBillEntity> taxBillEntities = taxBillRepo.findAllByBillId(billEntity.getId());
                    List<TaxBillDTO> taxBillDTOS = new ArrayList<>();

                    taxBillEntities.forEach(taxBillEntity -> {
                        Optional<TaxEntity> taxEntity = taxRepo.findById(taxBillEntity.getTaxId());
                        String taxName = taxEntity.get().getName();

                        TaxBillDTO taxBillDTO = TaxBillDTO.builder()
                                .name(taxName)
                                .price(taxBillEntity.getPrice())
                                .build();
                        taxBillDTOS.add(taxBillDTO);
                    });

                    DepartmentDTO departmentDTO = DepartmentDTO.builder()
                            .id(departmentEntity.getId())
                            .des(departmentEntity.getDescription())
                            .addressEntity(addressEntity)
                            .customerEntity(customerEntity)
                            .timelineEntities(timelineEntities).build();


                    BillBeforePaymentResponse billBeforePaymentResponse = billMapper.getResponseFromEntity(billEntity);
                    billBeforePaymentResponse
                            .setTaxs(taxBillDTOS);
                    billBeforePaymentResponse
                            .setDepartment(departmentDTO);

                    return billBeforePaymentResponse;
                }
        );
    }


}
