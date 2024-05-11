package com.example.electricitybillingsystem.service;

import com.example.electricitybillingsystem.model.TieredPricingEntity;
import com.example.electricitybillingsystem.repository.TieredPricingRepository;
import com.example.electricitybillingsystem.vo.request.UpdatePriceRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
class TieredPricingServiceTest {

    @Autowired
    private TieredPricingRepository tieredPricingRepository;

    @Autowired
    private TieredPricingService tieredPricingService;

    @Test
    @DisplayName("Cập nhât giá tiền - chỉ có giá tiền mới")
    void updatePrice() {
        Long electricityServiceId = 1L;
        List<TieredPricingEntity> newPrices = List.of(
                TieredPricingEntity.builder().startNumber(0L).endNumber(10L).value(BigDecimal.valueOf(7500)).electricityServiceId(electricityServiceId).build(),
                TieredPricingEntity.builder().startNumber(10L).endNumber(20L).value(BigDecimal.valueOf(8800)).electricityServiceId(electricityServiceId).build(),
                TieredPricingEntity.builder().startNumber(20L).endNumber(30L).value(BigDecimal.valueOf(12000)).electricityServiceId(electricityServiceId).build(),
                TieredPricingEntity.builder().startNumber(30L).value(BigDecimal.valueOf(24000)).electricityServiceId(electricityServiceId).build()
        );
        UpdatePriceRequest request = UpdatePriceRequest.builder()
                .currentPrices(List.of())
                .newPrices(newPrices)
                .build();

        tieredPricingService.updatePrice(request);

        List<TieredPricingEntity> prices = tieredPricingRepository.findAllByElectricityServiceId(1L);
        assertEquals(4, prices.size());
    }

}