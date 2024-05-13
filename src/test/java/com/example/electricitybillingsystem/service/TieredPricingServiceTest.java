package com.example.electricitybillingsystem.service;

import com.example.electricitybillingsystem.model.TieredPricingEntity;
import com.example.electricitybillingsystem.repository.TieredPricingRepository;
import com.example.electricitybillingsystem.vo.request.UpdatePriceRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@DisplayName("Chức năng cấu hình giá tiền")
class TieredPricingServiceTest {

    @Autowired
    private TieredPricingRepository tieredPricingRepository;

    @Autowired
    private TieredPricingService tieredPricingService;

    @Autowired
    private PlatformTransactionManager transactionManager;

    static Stream<List<UpdatePriceRequest.Price>> newPricesProvider() {
        return Stream.of(
                List.of(
                        UpdatePriceRequest.Price.builder().startNumber(0L).endNumber(10L).value(BigDecimal.valueOf(7500)).build(),
                        UpdatePriceRequest.Price.builder().startNumber(10L).endNumber(20L).value(BigDecimal.valueOf(8800)).build(),
                        UpdatePriceRequest.Price.builder().startNumber(20L).endNumber(30L).value(BigDecimal.valueOf(12000)).build(),
                        UpdatePriceRequest.Price.builder().startNumber(30L).value(BigDecimal.valueOf(24000)).build()
                ),
                List.of()
        );
    }

    @ParameterizedTest(name = "Đầu vào: {0}")
    @MethodSource("newPricesProvider")
    @DisplayName("Kiểm tra dữ liệu lưu vào db")
    void checkSaved(List<UpdatePriceRequest.Price> newPrices) {
        TransactionStatus transactionStatus = transactionManager.getTransaction(null);
        Long electricityServiceId = 1L;

        UpdatePriceRequest request = UpdatePriceRequest.builder()
                .electricityServiceId(electricityServiceId)
                .newPrices(newPrices)
                .build();

        tieredPricingService.updatePrice(request);

        List<TieredPricingEntity> prices = tieredPricingRepository.findAllByElectricityServiceId(1L);
        assertEquals(newPrices.size(), prices.size());

        transactionManager.rollback(transactionStatus);
    }


}
