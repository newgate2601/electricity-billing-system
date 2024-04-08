package com.example.electricitybillingsystem.service;

import com.example.electricitybillingsystem.vo.dto.*;
import com.example.electricitybillingsystem.mapper.BillMapper;
import com.example.electricitybillingsystem.model.*;
import com.example.electricitybillingsystem.repository.*;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
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
    private final TimelineRepository timelineRepository;
    private final TaxBillRepository taxBillRepository;
    private final TaxRepository taxRepository;

    private final TaxService taxService;
    private final TieredPricingHistoryService tieredPricingHistoryService;


    @Transactional(readOnly = true)
    public Page<BillBeforePaymentResponse> getAllBillBeforePayment(Pageable pageable) {
        Page<BillEntity> billEntities = billRepository.findAllByStatus(false, pageable);
        return billEntities.map(
                billEntity -> {
                    ApartmentEntity apartmentEntity = apartmentRepository.findById(billEntity.getApartmentId())
                            .orElseThrow(() -> new RuntimeException("NOT FOUND APARTMENT"));

                    AddressEntity addressEntity = addressRepository.findById(apartmentEntity.getAddressId())
                            .orElseThrow(() -> new RuntimeException("NOT FOUND ADDRESS"));
                    CustomerEntity customerEntity = customerRepository.findById(apartmentEntity.getCustomerId())
                            .orElseThrow(() -> new RuntimeException("NOT FOUND USER"));
                    List<TimelineEntity> timelineEntities = timelineRepository.findAllByApartmentId(apartmentEntity.getId());

                    List<TaxBillEntity> taxBillEntities = taxBillRepository.findAllByBillId(billEntity.getId());
                    List<TaxBillDTO> taxBillDTOS = new ArrayList<>();

                    taxBillEntities.forEach(taxBillEntity -> {
                        Optional<TaxEntity> taxEntity = taxRepository.findById(taxBillEntity.getTaxId());
                        String taxName = taxEntity.get().getName();

                        TaxBillDTO taxBillDTO = TaxBillDTO.builder()
                                .name(taxName)
                                .price(taxBillEntity.getPrice())
                                .build();
                        taxBillDTOS.add(taxBillDTO);
                    });

                    ApartmentDTO apartmentDTO = ApartmentDTO.builder()
                            .id(apartmentEntity.getId())
                            .des(apartmentEntity.getDescription())
                            .addressEntity(addressEntity)
                            .customerEntity(customerEntity)
                            .timelineEntities(timelineEntities).build();


                    BillBeforePaymentResponse billBeforePaymentResponse = billMapper.getResponseFromEntity(billEntity);
                    billBeforePaymentResponse
                            .setTaxs(taxBillDTOS);
                    billBeforePaymentResponse
                            .setApartment(apartmentDTO);

                    return billBeforePaymentResponse;
                }
        );
    }

    @Transactional(readOnly = true)
    public Page<BillAfterPaymentResponse> getAllBillAfterPayment(Pageable pageable) {
        Page<BillEntity> billEntities = billRepository.findAllByStatus(true, pageable);
        return billEntities.map(
                billEntity -> {
                    ApartmentEntity apartmentEntity = apartmentRepository.findById(billEntity.getApartmentId())
                            .orElseThrow(() -> new RuntimeException("NOT FOUND APARTMENT"));

                    AddressEntity addressEntity = addressRepository.findById(apartmentEntity.getAddressId())
                            .orElseThrow(() -> new RuntimeException("NOT FOUND ADDRESS"));
                    CustomerEntity customerEntity = customerRepository.findById(apartmentEntity.getCustomerId())
                            .orElseThrow(() -> new RuntimeException("NOT FOUND USER"));
                    List<TimelineEntity> timelineEntities = timelineRepository.findAllByApartmentId(apartmentEntity.getId());

                    List<TaxBillEntity> taxBillEntities = taxBillRepository.findAllByBillId(billEntity.getId());
                    List<TaxBillDTO> taxBillDTOS = new ArrayList<>();

                    taxBillEntities.forEach(taxBillEntity -> {
                        Optional<TaxEntity> taxEntity = taxRepository.findById(taxBillEntity.getTaxId());
                        String taxName = taxEntity.get().getName();

                        TaxBillDTO taxBillDTO = TaxBillDTO.builder()
                                .name(taxName)
                                .price(taxBillEntity.getPrice())
                                .build();
                        taxBillDTOS.add(taxBillDTO);
                    });

                    ApartmentDTO apartmentDTO = ApartmentDTO.builder()
                            .id(apartmentEntity.getId())
                            .des(apartmentEntity.getDescription())
                            .addressEntity(addressEntity)
                            .customerEntity(customerEntity)
                            .timelineEntities(timelineEntities).build();


                    BillAfterPaymentResponse billAfterPaymentResponse = billMapper.getResponseAfterFromEntity(billEntity);
                    billAfterPaymentResponse.setTaxs(taxBillDTOS);
                    billAfterPaymentResponse.setApartment(apartmentDTO);

                    return billAfterPaymentResponse;
                }
        );
    }

    @Transactional(readOnly = true)
    public List<DetailBillResponse> intoMoney(Long billId) {
        Long timelineStartNumber = billRepository.findById(billId).get().getStartNumber();
        Long timelineEndNumber  = billRepository.findById(billId).get().getEndNumber();
        List<TaxEntity> taxEntities = taxService.getAllTaxByBillId(billId);
        List<TieredPricingHistory> tieredPricingHistories = tieredPricingHistoryService.getAllTieredPricingHistoryByBillId(billId);

        tieredPricingHistories.sort((o1, o2) -> {
            if (o1.getStartNumber() > o2.getStartNumber()) {
                return 1;
            }
            if (o1.getStartNumber().equals(o2.getStartNumber())) {
                return 0;
            }
            return -1;
        });

        List<DetailBillResponse> detailBillResponses = new ArrayList<>();

        Long usedNumber = timelineEndNumber - timelineStartNumber; // 10 - 5  = 5 , 60 - 10 = 50
        BigDecimal finalPrice = BigDecimal.ZERO; // 0
        for (TieredPricingHistory item : tieredPricingHistories) { // 0-10
            DetailBillResponse detailBillResponse = new DetailBillResponse();
            if (usedNumber <= item.getEndNumber()) {
                Long used = Math.min(usedNumber, item.getEndNumber()) - item.getStartNumber() + 1;
                finalPrice = finalPrice.add(new BigDecimal(used).multiply(item.getPrice()));
                detailBillResponse.setUseWaterNumner(used);
                detailBillResponse.setPiceWater(item.getPrice());
                detailBillResponse.setCost(finalPrice);
                detailBillResponses.add(detailBillResponse);
                break;
            } else {
                Long used = item.getEndNumber() - item.getStartNumber() + 1;
                finalPrice = finalPrice.add(new BigDecimal(used).multiply(item.getPrice()));
                detailBillResponse.setUseWaterNumner(used);
                detailBillResponse.setPiceWater(item.getPrice());
                detailBillResponse.setCost(finalPrice);
                detailBillResponses.add(detailBillResponse);
                usedNumber -= used;
            }
        }

        for(TaxEntity item : taxEntities){
            DetailBillResponse detailBillResponse = new DetailBillResponse();
            finalPrice = finalPrice.add(finalPrice.multiply(item.getTax()));
            detailBillResponse.setCost(finalPrice);
            detailBillResponse.setTaxName(item.getName());
            detailBillResponse.setPriceTax(item.getTax());
            detailBillResponses.add(detailBillResponse);
        }

        return detailBillResponses;
    }

}
