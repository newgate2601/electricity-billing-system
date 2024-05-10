package com.example.electricitybillingsystem.service;

import com.example.electricitybillingsystem.vo.dto.ApartmentDTO;
import com.example.electricitybillingsystem.vo.dto.BillBeforePaymentResponse;
import com.example.electricitybillingsystem.vo.dto.TaxBillDTO;
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
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

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
    public Page<BillAfterPaymentResponse> getAllBillOverTime(Pageable pageable) {
        Page<BillEntity> billEntities = billRepository.findBillOverLimitedTime(pageable);
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
                                .price(taxBillEntity.getTax())
                                .build();
                        taxBillDTOS.add(taxBillDTO);
                    });

                    ApartmentDTO apartmentDTO = ApartmentDTO.builder()
                            .id(apartmentEntity.getId())
                            .des(apartmentEntity.getDescription())
                            .codeHome(apartmentEntity.getCode())
                            .addressEntity(addressEntity)
                            .customerEntity(customerEntity)
                            .timelineEntities(timelineEntities).build();


                    BillAfterPaymentResponse billAfterPaymentResponse = billMapper.getResponseAfterFromEntity(billEntity);
                    String limittimeConvert = convertOffsetToDate(billEntity.getLimitedTime());
                    billAfterPaymentResponse.setLimitedTimeResponse(limittimeConvert);
                    billAfterPaymentResponse.setTaxs(taxBillDTOS);
                    billAfterPaymentResponse.setApartment(apartmentDTO);

                    return billAfterPaymentResponse;
                }
        );
    }


    @Transactional(readOnly = true)
    public Page<BillBeforePaymentResponse> getAllBillBeforePayment(Pageable pageable) {
        Page<BillEntity> billEntities = billRepository.findAllBillBeforePayment(pageable);
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
                                .price(taxBillEntity.getTax())
                                .build();
                        taxBillDTOS.add(taxBillDTO);
                    });

                    ApartmentDTO apartmentDTO = ApartmentDTO.builder()
                            .id(apartmentEntity.getId())
                            .des(apartmentEntity.getDescription())
                            .codeHome(apartmentEntity.getCode())
                            .addressEntity(addressEntity)
                            .customerEntity(customerEntity)
                            .timelineEntities(timelineEntities).build();


                    BillBeforePaymentResponse billBeforePaymentResponse = billMapper.getResponseFromEntity(billEntity);
                    String limitedTimeConvert = convertOffsetToDate(billEntity.getLimitedTime());
                    billBeforePaymentResponse.setLimitedTimeResponse(limitedTimeConvert);
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
                                .price(taxBillEntity.getTax())
                                .build();
                        taxBillDTOS.add(taxBillDTO);
                    });

                    ApartmentDTO apartmentDTO = ApartmentDTO.builder()
                            .id(apartmentEntity.getId())
                            .des(apartmentEntity.getDescription())
                            .codeHome(apartmentEntity.getCode())
                            .addressEntity(addressEntity)
                            .customerEntity(customerEntity)
                            .timelineEntities(timelineEntities).build();

                    BillAfterPaymentResponse billAfterPaymentResponse = billMapper.getResponseAfterFromEntity(billEntity);
                    String limittimeConvert = convertOffsetToDate(billEntity.getLimitedTime());
                    String submittimeConvert = convertOffsetToDate(billEntity.getSubmitTime());
                    billAfterPaymentResponse.setLimitedTimeResponse(limittimeConvert);
                    billAfterPaymentResponse.setSubmitTimeResponse(submittimeConvert);
                    billAfterPaymentResponse.setTaxs(taxBillDTOS);
                    billAfterPaymentResponse.setApartment(apartmentDTO);

                    return billAfterPaymentResponse;
                }
        );
    }

    @Transactional(readOnly = true)
    public List<DetailBillResponse> intoMoney(Long billId) {
        List<DetailBillResponse> detailBillResponses = new ArrayList<>();
        Long timelineStartNumber = billRepository.findById(billId).get().getStartNumber();
        Long timelineEndNumber  = billRepository.findById(billId).get().getEndNumber();
        Long usedWater = timelineEndNumber - timelineStartNumber;
        detailBillResponses.add(DetailBillResponse.builder()
                .oldNumber(timelineStartNumber)
                .newNumber(timelineEndNumber)
                .usedWater(usedWater)
                .build());
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

        Long usedNumber = timelineEndNumber - timelineStartNumber;
        BigDecimal finalPrice = BigDecimal.ZERO;
        for (TieredPricingHistory item : tieredPricingHistories) {
            DetailBillResponse detailBillResponse = new DetailBillResponse();
            Long endNumber = item.getEndNumber();
            if (endNumber == null) {
                endNumber = Long.MAX_VALUE;
            }
            if (usedNumber < endNumber) {
                Long used = usedNumber;
                finalPrice = finalPrice.add(new BigDecimal(used).multiply(item.getPrice()));
                detailBillResponse.setWaterConsumption(used);
                detailBillResponse.setPiceWater(item.getPrice());
                detailBillResponse.setCost(new BigDecimal(used).multiply(item.getPrice()));
                detailBillResponses.add(detailBillResponse);

                break;
            } else {
                Long used = endNumber - item.getStartNumber();
                finalPrice = finalPrice.add(new BigDecimal(used).multiply(item.getPrice()));
                detailBillResponse.setWaterConsumption(used);
                detailBillResponse.setPiceWater(item.getPrice());
                detailBillResponse.setCost(new BigDecimal(used).multiply(item.getPrice()));
                detailBillResponses.add(detailBillResponse);
                usedNumber -= used;
            }
        }

        BigDecimal totalTax = BigDecimal.ZERO;
        for (TaxEntity item : taxEntities) {
            DetailBillResponse detailBillResponse = new DetailBillResponse();
            totalTax = totalTax.add(finalPrice.multiply(item.getTax().divide(new BigDecimal(100))));
            detailBillResponse.setCost(finalPrice.multiply(item.getTax().divide(new BigDecimal(100))));
            detailBillResponse.setTaxName(item.getName());
            detailBillResponse.setPriceTax(item.getTax());
            detailBillResponses.add(detailBillResponse);
        }
        finalPrice = finalPrice.add(totalTax);
        DetailBillResponse detailBillResponse = new DetailBillResponse();
        detailBillResponse.setFinalPrice(finalPrice);
        detailBillResponses.add(detailBillResponse);
        return detailBillResponses;
    }

    private String convertOffsetToDate(OffsetDateTime offsetDateTime){

        // Define a formatter
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        // Format the OffsetDateTime to a String
        String formattedDateTime = offsetDateTime.format(formatter);
        return formattedDateTime;
    }

}
