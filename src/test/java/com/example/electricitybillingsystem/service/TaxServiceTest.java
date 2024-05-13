package com.example.electricitybillingsystem.service;

import com.example.electricitybillingsystem.model.TaxEntity;
import com.example.electricitybillingsystem.repository.TaxRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@DisplayName("Chức năng cấu hình thuế")
class TaxServiceTest {

    @Autowired
    private TaxRepository taxRepository;

    @Autowired
    private TaxService taxService;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Autowired
    private EntityManager entityManager;

    static Stream<Object> taxValuesProvider() {
        return Stream.of(
                BigDecimal.valueOf(-1),
                BigDecimal.valueOf(0),
                BigDecimal.valueOf(10),
                BigDecimal.valueOf(12.5),
                BigDecimal.valueOf(100),
                BigDecimal.valueOf(101),
                "Kí tự không phải số"
        );
    }

    @ParameterizedTest(name = "Đầu vào: {0}")
    @MethodSource("taxValuesProvider")
    @DisplayName("Kiểm thử phân lớp tương đương giá trị thuế")
    @Transactional
    void saveTaxWithValidInput(Object tax) {
        TransactionStatus transactionStatus = transactionManager.getTransaction(null);

        // Fake data
        taxRepository.deleteAll();
        TaxEntity entity = taxRepository.save(TaxEntity.builder().id(1L).name("VAT").tax(BigDecimal.valueOf(10)).build());
        entityManager.flush();

        if (tax instanceof BigDecimal && ((BigDecimal) tax).compareTo(BigDecimal.ZERO) > 0 && ((BigDecimal) tax).compareTo(BigDecimal.valueOf(100)) < 0) {
            taxService.save(entity.getId(), (BigDecimal) tax);

            TaxEntity savedTaxEntity = taxRepository.findById(entity.getId()).orElseThrow();
            assertEquals(tax, savedTaxEntity.getTax());
        } else if (!(tax instanceof BigDecimal)) {
            assertThrows(ClassCastException.class, () -> taxService.save(entity.getId(), (BigDecimal) tax));
        } else {
            assertThrows(IllegalArgumentException.class, () -> taxService.save(entity.getId(), (BigDecimal) tax));
        }

        // Rollback
        transactionManager.rollback(transactionStatus);
    }

}