package com.example.electricitybillingsystem.service;


import com.example.electricitybillingsystem.model.BillEntity;
import com.example.electricitybillingsystem.model.CustomerEntity;
import com.example.electricitybillingsystem.repository.BillRepository;
import com.example.electricitybillingsystem.repository.CustomerRepository;
import com.example.electricitybillingsystem.vo.dto.BillResponse;
import io.jsonwebtoken.lang.Assert;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;

import java.awt.print.Pageable;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class BillServiceTest {
    @Autowired
    private BillService2 billService2;
    @Autowired
    private BillRepository billRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private PlatformTransactionManager transactionManager;

    @Test
    @DisplayName("Lấy danh sách bill theo tháng hiện tại")
    @Transactional
    public void testGetBills(){
        TransactionStatus transaction = this.transactionManager.getTransaction(null);
        fakeDataForTestGetBills();

        Page<BillResponse> billResponses = billService2.getBills(
                null,
                null,
                null,
                null,
                null,
                PageRequest.of(0, 1000000));

        // compare size bills in db not filter
        assertEquals(billResponses.getContent().size(), 3);

        // rollback
        this.transactionManager.rollback(transaction); // A, B, C
    }

    void fakeDataForTestGetBills(){
        //        RuntimeException exception = Assertions.assertThrows(
//                RuntimeException.class, () -> categoryService.create(categoryDto1)
        // A, B, C
        billRepository.deleteAll();
        customerRepository.deleteAll();
        // 0 record DONE, YET

        // fake data customer for testing get bills
        CustomerEntity phu = CustomerEntity.builder()
                .name("phú")
                .build();
        customerRepository.save(phu);

        CustomerEntity phuc = CustomerEntity.builder()
                .name("phúc")
                .build();
        customerRepository.save(phuc);

        CustomerEntity ngoc = CustomerEntity.builder()
                .name("ngọc")
                .build();
        customerRepository.save(ngoc);

        // fake data for bills
        BillEntity billPhu_03_2024 = BillEntity.builder()
                .month(3)
                .year(2024)
                .customerId(phu.getId())
                .price(BigDecimal.valueOf(1))
                .statusValue("DONE")
                .build();
        billRepository.save(billPhu_03_2024);

        BillEntity billPhu_04_2024 = BillEntity.builder()
                .month(4)
                .year(2024)
                .customerId(phu.getId())
                .price(BigDecimal.valueOf(1))
                .statusValue("DONE")
                .build();
        billRepository.save(billPhu_04_2024);

        BillEntity billPhu_05_2024 = BillEntity.builder()
                .month(5)
                .year(2024)
                .customerId(phu.getId())
                .price(BigDecimal.valueOf(1))
                .statusValue("YET")
                .build();
        billRepository.save(billPhu_05_2024);

        BillEntity billPhuc_03_2024 = BillEntity.builder()
                .month(3)
                .year(2024)
                .customerId(phuc.getId())
                .price(BigDecimal.valueOf(2))
                .statusValue("DONE")
                .build();
        billRepository.save(billPhuc_03_2024);

        BillEntity billPhuc_04_2024 = BillEntity.builder()
                .month(4)
                .year(2024)
                .customerId(phuc.getId())
                .price(BigDecimal.valueOf(2))
                .statusValue("YET")
                .build();
        billRepository.save(billPhuc_04_2024);

        BillEntity billPhuc_05_2024 = BillEntity.builder()
                .month(5)
                .year(2024)
                .customerId(phuc.getId())
                .price(BigDecimal.valueOf(2))
                .statusValue("YET")
                .build();
        billRepository.save(billPhuc_05_2024);

        BillEntity billNgoc_03_2024 = BillEntity.builder()
                .month(3)
                .year(2024)
                .customerId(phuc.getId())
                .price(BigDecimal.valueOf(3))
                .statusValue("YET")
                .build();
        billRepository.save(billNgoc_03_2024);

        BillEntity billNgoc_04_2024 = BillEntity.builder()
                .month(4)
                .year(2024)
                .customerId(phuc.getId())
                .price(BigDecimal.valueOf(3))
                .statusValue("YET")
                .build();
        billRepository.save(billNgoc_04_2024);

        BillEntity billNgoc_05_2024 = BillEntity.builder()
                .month(5)
                .year(2024)
                .customerId(phuc.getId())
                .price(BigDecimal.valueOf(3))
                .statusValue("YET")
                .build();
        billRepository.save(billNgoc_05_2024);

        // flush to db
        entityManager.flush();
    }

}
