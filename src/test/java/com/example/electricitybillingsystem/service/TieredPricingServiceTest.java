package com.example.electricitybillingsystem.service;

import com.example.electricitybillingsystem.model.TieredPricingEntity;
import com.example.electricitybillingsystem.repository.TieredPricingRepository;
import com.example.electricitybillingsystem.vo.request.UpdatePriceRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class TieredPricingServiceTest {

    @Autowired
    private TieredPricingService tieredPricingService;

    @MockBean
    private TieredPricingRepository tieredPricingRepository;

    @Test
    @DisplayName("Cập nhât giá tiền - chỉ có giá tiền mới")
    void updatePrice() {
        UpdatePriceRequest request = UpdatePriceRequest.builder()
                .currentPrices(List.of())
                .newPrices(List.of(
                        TieredPricingEntity.builder()
                                .startNumber(0L)
                                .endNumber(10L)
                                .value(BigDecimal.valueOf(7500))
                                .electricityServiceId(1L)
                                .build(),
                        TieredPricingEntity.builder()
                                .startNumber(10L)
                                .endNumber(20L)
                                .value(BigDecimal.valueOf(8800))
                                .electricityServiceId(1L)
                                .build(),
                        TieredPricingEntity.builder()
                                .startNumber(20L)
                                .endNumber(30L)
                                .value(BigDecimal.valueOf(12000))
                                .electricityServiceId(1L)
                                .build(),
                        TieredPricingEntity.builder()
                                .startNumber(30L)
                                .value(BigDecimal.valueOf(24000))
                                .electricityServiceId(1L)
                                .build()
                ))
                .build();
        tieredPricingService.updatePrice(request);
        List<TieredPricingEntity> prices = tieredPricingRepository.findAllByElectricityServiceId(1L);
        assertEquals(4, prices.size());
    }
}