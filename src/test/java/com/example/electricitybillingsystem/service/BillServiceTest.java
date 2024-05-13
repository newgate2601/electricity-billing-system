package com.example.electricitybillingsystem.service;


import com.example.electricitybillingsystem.model.*;
import com.example.electricitybillingsystem.repository.*;
import com.example.electricitybillingsystem.vo.dto.BillBeforePaymentResponse;
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
import java.time.Duration;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class BillServiceTest {
    @Autowired
    private BillService2 billService2;
    @Autowired
    private BillService billService;
    @Autowired
    private BillRepository billRepository;
    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private ApartmentRepository apartmentRepository;

    @Autowired
    private TimelineRepository timelineRepository;

    @Autowired
    private  TaxRepository taxRepository;

    @Autowired
    private  TaxBillRepository taxBillRepository;
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private PlatformTransactionManager transactionManager;

    @Test
    @DisplayName("Lấy danh sách bill theo tháng hiện tại")
    @Transactional
    public void testGetBills() {
        TransactionStatus transaction = this.transactionManager.getTransaction(null);
        fakeDataForTestGetBills();

        Page<BillResponse> billResponses = billService2.getBills(
                null,
                null,
                null,
                null,
                null,
                PageRequest.of(0, 1000000));

        showResultAfterFilter(billResponses);

        // compare size bills in db not filter
        assertEquals(3, billResponses.getContent().size());

        // rollback
        this.transactionManager.rollback(transaction); // A, B, C
    }

    @Test
    @DisplayName("Lấy danh sách bill theo tháng 3")
    @Transactional
    public void testGetBillsV2() {
        TransactionStatus transaction = this.transactionManager.getTransaction(null);
        fakeDataForTestGetBills();

        Page<BillResponse> billResponses = billService2.getBills(
                null,
                null,
                3,
                2024,
                null,
                PageRequest.of(0, 1000000));

        showResultAfterFilter(billResponses);

        // compare size bills in db not filter
        assertEquals(3, billResponses.getContent().size());

        // rollback
        this.transactionManager.rollback(transaction);
    }

    @Test
    @DisplayName("Lấy danh sách bill theo tháng 4 và sắp xếp theo thứ tự giảm dần giá bill")
    @Transactional
    public void testGetBillsV3() {
        TransactionStatus transaction = this.transactionManager.getTransaction(null);
        fakeDataForTestGetBills();

        Page<BillResponse> billResponses = billService2.getBills(
                null,
                null,
                4,
                2024,
                "DESC",
                PageRequest.of(0, 1000000));

        showResultAfterFilter(billResponses);

        assertEquals(billResponses.getContent().size(), 3);
        List<BillResponse> bills = billResponses.getContent();
        assertEquals(bills.get(0).getPrice(), BigDecimal.valueOf(3));
        assertEquals(bills.get(0).getCustomerName(), "ngọc");

        assertEquals(bills.get(1).getPrice(), BigDecimal.valueOf(2));
        assertEquals(bills.get(1).getCustomerName(), "phúc");

        assertEquals(bills.get(2).getPrice(), BigDecimal.valueOf(1));
        assertEquals(bills.get(2).getCustomerName(), "phú");

        // rollback
        this.transactionManager.rollback(transaction);
    }

    @Test
    @DisplayName("Lấy danh sách bill theo tháng 6")
    @Transactional
    public void testGetBillsV4() {
        TransactionStatus transaction = this.transactionManager.getTransaction(null);
        fakeDataForTestGetBills();

        Page<BillResponse> billResponses = billService2.getBills(
                null,
                null,
                6,
                2024,
                null,
                PageRequest.of(0, 1000000));

        showResultAfterFilter(billResponses);

        // compare size bills in db not filter
        assertEquals(0, billResponses.getContent().size());

        // rollback
        this.transactionManager.rollback(transaction);
    }

    @Test
    @DisplayName("Lấy danh sách bill theo tháng 3 của những người dùng có tên gồm chữ cái #ú ")
    @Transactional
    public void testGetBillsV5() {
        TransactionStatus transaction = this.transactionManager.getTransaction(null);
        fakeDataForTestGetBills();

        Page<BillResponse> billResponses = billService2.getBills(
                null,
                "ú",
                3,
                2024,
                null,
                PageRequest.of(0, 1000000));

        showResultAfterFilter(billResponses);

        // compare size bills in db not filter
        assertEquals(2, billResponses.getContent().size());

        // rollback
        this.transactionManager.rollback(transaction);
    }

    @Test
    @DisplayName("Lấy danh sách bill theo tháng 3 của những người dùng có tên gồm chữ cái #ú " +
            "và trạng thái là chưa đóng tiền")
    @Transactional
    public void testGetBillsV6() {
        TransactionStatus transaction = this.transactionManager.getTransaction(null);
        fakeDataForTestGetBills();

        Page<BillResponse> billResponses = billService2.getBills(
                "YET",
                "ú",
                3,
                2024,
                null,
                PageRequest.of(0, 1000000));

        showResultAfterFilter(billResponses);

        // compare size bills in db not filter
        assertEquals(0, billResponses.getContent().size());

        // rollback
        this.transactionManager.rollback(transaction);
    }

    @Test
    @DisplayName("Lấy danh sách bill theo tháng 3 của những người dùng có tên gồm chữ cái #ú " +
            "và trạng thái là đã đóng tiền " +
            "và sắp xếp theo giá tiền giảm dần")
    @Transactional
    public void testGetBillsV7() {
        TransactionStatus transaction = this.transactionManager.getTransaction(null);
        fakeDataForTestGetBills();

        Page<BillResponse> billResponses = billService2.getBills(
                "DONE",
                "ú",
                3,
                2024,
                "DESC",
                PageRequest.of(0, 1000000));

        showResultAfterFilter(billResponses);

        // compare size bills in db not filter
        assertEquals(2, billResponses.getContent().size());

        List<BillResponse> bills = billResponses.getContent();
        assertEquals(BigDecimal.valueOf(2), bills.get(0).getPrice());
        assertEquals("phúc", bills.get(0).getCustomerName());

        assertEquals(BigDecimal.valueOf(1), bills.get(1).getPrice());
        assertEquals("phú", bills.get(1).getCustomerName());

        // rollback
        this.transactionManager.rollback(transaction);
    }

    @Test
    @DisplayName("Lấy danh sách bill theo tháng 3 của những người dùng có tên gồm chữ cái #z ")
    @Transactional
    public void testGetBillsV8() {
        TransactionStatus transaction = this.transactionManager.getTransaction(null);
        fakeDataForTestGetBills();

        Page<BillResponse> billResponses = billService2.getBills(
                null,
                "z",
                3,
                2024,
                null,
                PageRequest.of(0, 1000000));

        showResultAfterFilter(billResponses);

        assertEquals(0, billResponses.getContent().size());

        // rollback
        this.transactionManager.rollback(transaction);
    }

    @Test
    @DisplayName("Lấy danh sách bill theo tháng 12 của những người dùng có tên gồm chữ cái #q ")
    @Transactional
    public void testGetBillsV9() {
        TransactionStatus transaction = this.transactionManager.getTransaction(null);
        fakeDataForTestGetBills();

        Page<BillResponse> billResponses = billService2.getBills(
                null,
                "q",
                12,
                2024,
                null,
                PageRequest.of(0, 1000000));

        showResultAfterFilter(billResponses);

        // compare size bills in db not filter
        assertEquals(0, billResponses.getContent().size());

        // rollback
        this.transactionManager.rollback(transaction);
    }

    @Test
    @DisplayName("Lấy danh sách bill theo tháng 5 của những người dùng có tên gồm chữ cái #c " +
            "và sắp xếp theo thứ tự giá giảm dần")
    @Transactional
    public void testGetBillsV10() {
        TransactionStatus transaction = this.transactionManager.getTransaction(null);
        fakeDataForTestGetBills();

        Page<BillResponse> billResponses = billService2.getBills(
                null,
                "c",
                5,
                2024,
                "DESC",
                PageRequest.of(0, 1000000));

        showResultAfterFilter(billResponses);

        // compare size bills in db not filter
        assertEquals(2, billResponses.getContent().size());

        List<BillResponse> bills = billResponses.getContent();
        assertEquals(BigDecimal.valueOf(3), bills.get(0).getPrice());
        assertEquals("ngọc", bills.get(0).getCustomerName());

        assertEquals(BigDecimal.valueOf(2), bills.get(1).getPrice());
        assertEquals("phúc", bills.get(1).getCustomerName());
        // rollback
        this.transactionManager.rollback(transaction);
    }

    @Test
    @DisplayName("Lấy danh sách bill theo tháng 3 " +
            "và sắp xếp theo thứ tự giá giảm dần")
    @Transactional
    public void testGetBillsV11() {
        TransactionStatus transaction = this.transactionManager.getTransaction(null);
        fakeDataForTestGetBills();

        Page<BillResponse> billResponses = billService2.getBills(
                null,
                null,
                3,
                2024,
                "DESC",
                PageRequest.of(0, 1000000));

        showResultAfterFilter(billResponses);

        assertEquals(3, billResponses.getContent().size());

        List<BillResponse> bills = billResponses.getContent();
        assertEquals(BigDecimal.valueOf(3), bills.get(0).getPrice());
        assertEquals("ngọc", bills.get(0).getCustomerName());

        assertEquals(BigDecimal.valueOf(2), bills.get(1).getPrice());
        assertEquals("phúc", bills.get(1).getCustomerName());

        assertEquals(BigDecimal.valueOf(1), bills.get(2).getPrice());
        assertEquals("phú", bills.get(2).getCustomerName());

        // rollback
        this.transactionManager.rollback(transaction);
    }

    @Test
    @DisplayName("Lấy danh sách bill theo tháng 5 của những người dùng có tên gồm chữ cái #c " +
            "và sắp xếp theo thứ tự giá tăng dần")
    @Transactional
    public void testGetBillsV12() {
        TransactionStatus transaction = this.transactionManager.getTransaction(null);
        fakeDataForTestGetBills();

        Page<BillResponse> billResponses = billService2.getBills(
                null,
                "c",
                5,
                2024,
                "ASC",
                PageRequest.of(0, 1000000));

        showResultAfterFilter(billResponses);

        // compare size bills in db not filter
        assertEquals(2, billResponses.getContent().size());

        List<BillResponse> bills = billResponses.getContent();
        assertEquals(BigDecimal.valueOf(2), bills.get(0).getPrice());
        assertEquals("phúc", bills.get(0).getCustomerName());

        assertEquals(BigDecimal.valueOf(3), bills.get(1).getPrice());
        assertEquals("ngọc", bills.get(1).getCustomerName());

        // rollback
        this.transactionManager.rollback(transaction);
    }

    @Test
    @DisplayName("Lấy danh sách bill theo tháng 12 của những người dùng có tên gồm chữ cái #k " +
            "và theo thứ tự giảm dần " +
            "và trạng thái là đã đóng tiền")
    @Transactional
    public void testGetBillsV13() {
        TransactionStatus transaction = this.transactionManager.getTransaction(null);
        fakeDataForTestGetBills();

        Page<BillResponse> billResponses = billService2.getBills(
                null,
                "k",
                12,
                2024,
                "DESC",
                PageRequest.of(0, 1000000));

        showResultAfterFilter(billResponses);

        // compare size bills in db not filter
        assertEquals(0, billResponses.getContent().size());

        // rollback
        this.transactionManager.rollback(transaction);
    }

    @Test
    @DisplayName("Lấy danh sách bill theo tháng 5 của những người dùng có tên gồm chữ cái #c " +
            "và sắp xếp theo thứ tự giá tăng dần " +
            "và trạng thái đóng tiền là đã đóng")
    @Transactional
    public void testGetBillsV14() {
        TransactionStatus transaction = this.transactionManager.getTransaction(null);
        fakeDataForTestGetBills();

        Page<BillResponse> billResponses = billService2.getBills(
                null,
                "c",
                5,
                2024,
                "ASC",
                PageRequest.of(0, 1000000));

        showResultAfterFilter(billResponses);

        // compare size bills in db not filter
        assertEquals(2, billResponses.getContent().size());

        List<BillResponse> bills = billResponses.getContent();
        assertEquals(BigDecimal.valueOf(2), bills.get(0).getPrice());
        assertEquals("phúc", bills.get(0).getCustomerName());

        assertEquals(BigDecimal.valueOf(3), bills.get(1).getPrice());
        assertEquals("ngọc", bills.get(1).getCustomerName());

        // rollback
        this.transactionManager.rollback(transaction);
    }

    @Test
    @DisplayName("Lấy danh sách bill theo tháng 5 của những người dùng có tên gồm chữ cái #c " +
            "và sắp xếp theo thứ tự giá giảm dần " +
            "và trạng thái đóng tiền là đã đóng")
    @Transactional
    public void testGetBillsV15() {
        TransactionStatus transaction = this.transactionManager.getTransaction(null);
        fakeDataForTestGetBills();

        Page<BillResponse> billResponses = billService2.getBills(
                "DONE",
                "c",
                5,
                2024,
                "DESC",
                PageRequest.of(0, 1000000));

        showResultAfterFilter(billResponses);

        // compare size bills in db not filter
        assertEquals(0, billResponses.getContent().size());

//        List<BillResponse> bills = billResponses.getContent();
//        assertEquals(BigDecimal.valueOf(3), bills.get(0).getPrice());
//        assertEquals("ngọc", bills.get(0).getCustomerName());
//
//        assertEquals(BigDecimal.valueOf(2), bills.get(1).getPrice());
//        assertEquals("phúc", bills.get(1).getCustomerName());

        // rollback
        this.transactionManager.rollback(transaction);
    }

    @Test
    @DisplayName("Lấy danh sách bill theo tháng 5 của những người dùng có tên gồm chữ cái #p " +
            "và sắp xếp theo thứ tự giá giảm dần " +
            "và trạng thái đóng tiền là đã đóng")
    @Transactional
    public void testGetBillsV16() {
        TransactionStatus transaction = this.transactionManager.getTransaction(null);
        fakeDataForTestGetBills();

        Page<BillResponse> billResponses = billService2.getBills(
                "DONE",
                "p",
                5,
                2024,
                "DESC",
                PageRequest.of(0, 1000000));

        showResultAfterFilter(billResponses);

        // compare size bills in db not filter
        assertEquals(0, billResponses.getContent().size());

//        List<BillResponse> bills = billResponses.getContent();
//        assertEquals(BigDecimal.valueOf(2), bills.get(0).getPrice());
//        assertEquals("phúc", bills.get(0).getCustomerName());
//
//        assertEquals(BigDecimal.valueOf(1), bills.get(1).getPrice());
//        assertEquals("phú", bills.get(1).getCustomerName());

        // rollback
        this.transactionManager.rollback(transaction);
    }

    @Test
    @DisplayName("Lấy danh sách bill theo tháng 5 của những người dùng có tên gồm chữ cái #c " +
            "và sắp xếp theo thứ tự giá giảm dần " +
            "và trạng thái đóng tiền là chưa đóng")
    @Transactional
    public void testGetBillsV17() {
        TransactionStatus transaction = this.transactionManager.getTransaction(null);
        fakeDataForTestGetBills();

        Page<BillResponse> billResponses = billService2.getBills(
                "YET",
                "c",
                5,
                2024,
                "DESC",
                PageRequest.of(0, 1000000));

        showResultAfterFilter(billResponses);

        // compare size bills in db not filter
        assertEquals(2, billResponses.getContent().size());

        List<BillResponse> bills = billResponses.getContent();
        assertEquals(BigDecimal.valueOf(3), bills.get(0).getPrice());
        assertEquals("ngọc", bills.get(0).getCustomerName());

        assertEquals(BigDecimal.valueOf(2), bills.get(1).getPrice());
        assertEquals("phúc", bills.get(1).getCustomerName());

        // rollback
        this.transactionManager.rollback(transaction);
    }

    @Test
    @DisplayName("Lấy danh sách bill theo tháng 5 của những người dùng có tên gồm chữ cái #c " +
            "và sắp xếp theo thứ tự giá tăng dần " +
            "và trạng thái đóng tiền là chưa đóng")
    @Transactional
    public void testGetBillsV18() {
        TransactionStatus transaction = this.transactionManager.getTransaction(null);
        fakeDataForTestGetBills();

        Page<BillResponse> billResponses = billService2.getBills(
                "YET",
                "c",
                5,
                2024,
                "DESC",
                PageRequest.of(0, 1000000));

        showResultAfterFilter(billResponses);

        // compare size bills in db not filter
        assertEquals(2, billResponses.getContent().size());

        List<BillResponse> bills = billResponses.getContent();
        assertEquals(BigDecimal.valueOf(3), bills.get(0).getPrice());
        assertEquals("ngọc", bills.get(0).getCustomerName());

        assertEquals(BigDecimal.valueOf(2), bills.get(1).getPrice());
        assertEquals("phúc", bills.get(1).getCustomerName());

        // rollback
        this.transactionManager.rollback(transaction);
    }

    @Test
    @DisplayName("Lấy danh sách bill theo tháng 4 của những người dùng có tên gồm chữ cái #p")
    @Transactional
    public void testGetBillsV19() {
        TransactionStatus transaction = this.transactionManager.getTransaction(null);
        fakeDataForTestGetBills();

        Page<BillResponse> billResponses = billService2.getBills(
                null,
                "p",
                4,
                2024,
                null,
                PageRequest.of(0, 1000000));

        showResultAfterFilter(billResponses);

        // compare size bills in db not filter
        assertEquals(2, billResponses.getContent().size());

        // rollback
        this.transactionManager.rollback(transaction);
    }

    @Test
    @DisplayName("Lấy danh sách bill theo tháng 4 của những người dùng có trạng thái đã đóng tiền")
    @Transactional
    public void testGetBillsV20() {
        TransactionStatus transaction = this.transactionManager.getTransaction(null);
        fakeDataForTestGetBills();

        Page<BillResponse> billResponses = billService2.getBills(
                "DONE",
                null,
                4,
                2024,
                null,
                PageRequest.of(0, 1000000));

        showResultAfterFilter(billResponses);

        // compare size bills in db not filter
        assertEquals(1, billResponses.getContent().size());
        List<BillResponse> bills = billResponses.getContent();
        assertEquals(BigDecimal.valueOf(1), bills.get(0).getPrice());
        assertEquals("phú", bills.get(0).getCustomerName());

        // rollback
        this.transactionManager.rollback(transaction);
    }

    void showResultAfterFilter(Page<BillResponse> billResponses) {
        System.out.println("-------------------------------------DATA AFTER FILTER:");
        for (BillResponse billResponse : billResponses.getContent()) {
            System.out.println(billResponse.getId() +
                    " - price: " + billResponse.getPrice() +
                    " - status: " + billResponse.getStatus() +
                    " - customerName: " + billResponse.getCustomerName());
        }
        System.out.println("-------------------------------------------------------");
    }



    void fakeDataForTestGetBills() {
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
                .customerId(ngoc.getId())
                .price(BigDecimal.valueOf(3))
                .statusValue("YET")
                .build();
        billRepository.save(billNgoc_03_2024);

        BillEntity billNgoc_04_2024 = BillEntity.builder()
                .month(4)
                .year(2024)
                .customerId(ngoc.getId())
                .price(BigDecimal.valueOf(3))
                .statusValue("YET")
                .build();
        billRepository.save(billNgoc_04_2024);

        BillEntity billNgoc_05_2024 = BillEntity.builder()
                .month(5)
                .year(2024)
                .customerId(ngoc.getId())
                .price(BigDecimal.valueOf(3))
                .statusValue("YET")
                .build();
        billRepository.save(billNgoc_05_2024);

        // flush to db
        entityManager.flush();
    }


}
