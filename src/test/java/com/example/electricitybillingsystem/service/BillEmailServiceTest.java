package com.example.electricitybillingsystem.service;

import com.example.electricitybillingsystem.model.*;
import com.example.electricitybillingsystem.repository.*;
import com.example.electricitybillingsystem.vo.dto.BillAfterPaymentResponse;
import com.example.electricitybillingsystem.vo.dto.BillBeforePaymentResponse;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
public class BillEmailServiceTest {
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
    void initData() {
        billRepository.deleteAll();
        customerRepository.deleteAll();
        addressRepository.deleteAll();
        taxBillRepository.deleteAll();
        taxRepository.deleteAll();
        apartmentRepository.deleteAll();
        timelineRepository.deleteAll();

        AddressEntity addressPhuc = AddressEntity.builder()
                .city("Ha Noi")
                .district("Ha Dong")
                .ward("Van Quan")
                .build();
        addressRepository.save(addressPhuc);

        TaxEntity taxEntity = TaxEntity.builder()
                .name("VAT")
                .tax(BigDecimal.valueOf(5))
                .build();
        taxRepository.save(taxEntity);
        CustomerEntity customerPhuc = CustomerEntity.builder()
                .name("Phúc")
                .build();
        customerRepository.save(customerPhuc);
        ApartmentEntity apartmentPhuc = ApartmentEntity.builder()
                .code("HD1")
                .addressId(addressPhuc.getId())
                .customerId(customerPhuc.getId())
                .build();
        apartmentRepository.save(apartmentPhuc);

        TimelineEntity timelineEntity = TimelineEntity.builder()
                .startNumber(10L)
                .endNumber(20L)
                .apartmentId(apartmentPhuc.getId())
                .build();
        timelineRepository.save(timelineEntity);
        BillEntity bill_phuc = BillEntity.builder()
                .status(false)
                .customerId(customerPhuc.getId())
                .apartmentId(apartmentPhuc.getId())
                .limitedTime(OffsetDateTime.of(2024, 5, 15, 10, 30, 0, 0, ZoneOffset.UTC))
                .customerId(customerPhuc.getId())
                .build();
        billRepository.save(bill_phuc);
        TaxBillEntity taxBillEntity = TaxBillEntity.builder()
                .billId(bill_phuc.getId())
                .taxId(taxEntity.getId())
                .tax(BigDecimal.valueOf(5))
                .build();
        taxBillRepository.save(taxBillEntity);

        // case khách hang qua han
        AddressEntity addressPhu = AddressEntity.builder()
                .city("Ha Noi")
                .district("Ha Dong")
                .ward("Van Quan")
                .build();
        addressRepository.save(addressPhu);

        TaxEntity taxPhu = TaxEntity.builder()
                .name("VAT")
                .tax(BigDecimal.valueOf(5))
                .build();
        taxRepository.save(taxPhu);
        CustomerEntity customerPhu = CustomerEntity.builder()
                .name("Phú")
                .build();
        customerRepository.save(customerPhu);
        ApartmentEntity apartmentPhu = ApartmentEntity.builder()
                .code("HD3")
                .addressId(addressPhu.getId())
                .customerId(customerPhu.getId())
                .build();
        apartmentRepository.save(apartmentPhu);

        TimelineEntity timelinePhu = TimelineEntity.builder()
                .startNumber(10L)
                .endNumber(20L)
                .apartmentId(apartmentPhu.getId())
                .build();
        timelineRepository.save(timelinePhu);
        BillEntity bill_phu = BillEntity.builder()
                .status(false)
                .customerId(customerPhu.getId())
                .apartmentId(apartmentPhu.getId())
                .limitedTime(OffsetDateTime.of(2024, 5, 4, 10, 30, 0, 0, ZoneOffset.UTC))
                .customerId(customerPhu.getId())
                .build();
        billRepository.save(bill_phu);
        TaxBillEntity taxBillPhu = TaxBillEntity.builder()
                .billId(bill_phu.getId())
                .taxId(taxEntity.getId())
                .tax(BigDecimal.valueOf(5))
                .build();
        taxBillRepository.save(taxBillPhu);

        // case khách hàng da thanh toan
        AddressEntity addressNgoc = AddressEntity.builder()
                .city("Ha Noi")
                .district("Ha Dong")
                .ward("Mo Lao")
                .build();
        addressRepository.save(addressNgoc);

        TaxEntity taxNgoc = TaxEntity.builder()
                .name("VAT")
                .tax(BigDecimal.valueOf(5))
                .build();
        taxRepository.save(taxNgoc);
        CustomerEntity customerNgoc = CustomerEntity.builder()
                .name("Ngọc")
                .build();
        customerRepository.save(customerNgoc);
        ApartmentEntity apartmentNgoc = ApartmentEntity.builder()
                .code("HD2")
                .addressId(addressNgoc.getId())
                .customerId(customerNgoc.getId())
                .build();
        apartmentRepository.save(apartmentNgoc);

        TimelineEntity timelineNgoc = TimelineEntity.builder()
                .startNumber(10L)
                .endNumber(20L)
                .apartmentId(apartmentNgoc.getId())
                .build();
        timelineRepository.save(timelineNgoc);
        BillEntity bill_ngoc = BillEntity.builder()
                .status(true)
                .customerId(customerNgoc.getId())
                .apartmentId(apartmentNgoc.getId())
                .limitedTime(OffsetDateTime.of(2024, 5, 15, 10, 30, 0, 0, ZoneOffset.UTC))
                .submitTime(OffsetDateTime.of(2024, 5, 12, 10, 30, 0, 0, ZoneOffset.UTC))
                .customerId(customerNgoc.getId())
                .build();
        billRepository.save(bill_ngoc);
        TaxBillEntity taxBillNgoc = TaxBillEntity.builder()
                .billId(bill_ngoc.getId())
                .taxId(taxEntity.getId())
                .tax(BigDecimal.valueOf(5))
                .build();
        taxBillRepository.save(taxBillNgoc);
        entityManager.flush();
    }

    @Test
    @Transactional
    @DisplayName("test lấy bill trước khi thanh toán có ngày giới hạn nhỏ hơn 0 ")
    public void testGetAllBillBeforePayment_fail() {
        // Tạo dữ liệu thử nghiệm
        TransactionStatus transaction = this.transactionManager.getTransaction(null);
        initData();
        Page<BillBeforePaymentResponse> billResponses = billService.getAllBillBeforePayment(

                PageRequest.of(0, 1000000));

        // compare size bills in db not filter
        assertEquals(1, billResponses.getContent().size());
        List<BillBeforePaymentResponse> bills = billResponses.getContent();
        assertEquals(false, bills.get(0).getStatus());
        // Kiểm tra ngày hiện tại trừ limitedTime > 7
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy'T'HH:mm:ssXXX");

        // Phân tích chuỗi thành OffsetDateTime
        OffsetDateTime limitedTime = OffsetDateTime.parse(bills.get(0).getLimitedTimeResponse()+"T00:00:00+00:00", formatter);

        OffsetDateTime currentTime = OffsetDateTime.parse("17-05-2024"+"T00:00:00+00:00", formatter);
        Duration duration = Duration.between(currentTime, limitedTime);
        Long days = duration.toDays();
        assertFalse( duration.toDays() > 0);
        // rollback
        this.transactionManager.rollback(transaction);


    }

    @Test
    @Transactional
    @DisplayName("test lấy bill trước khi thanh toán có ngày giới hạn lớn hơn 0 ")
    public void testGetAllBillBeforePayment() {
        // Tạo dữ liệu thử nghiệm
        TransactionStatus transaction = this.transactionManager.getTransaction(null);
        initData();
        Page<BillBeforePaymentResponse> billResponses = billService.getAllBillBeforePayment(

                PageRequest.of(0, 1000000));

        // compare size bills in db not filter
        assertEquals(1, billResponses.getContent().size());
        List<BillBeforePaymentResponse> bills = billResponses.getContent();
        assertEquals(false, bills.get(0).getStatus());
        // Kiểm tra ngày hiện tại trừ limitedTime > 7
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy'T'HH:mm:ssXXX");

        // Phân tích chuỗi thành OffsetDateTime
        OffsetDateTime limitedTime = OffsetDateTime.parse(bills.get(0).getLimitedTimeResponse()+"T00:00:00+00:00", formatter);

        OffsetDateTime currentTime = OffsetDateTime.now();
        Duration duration = Duration.between(currentTime, limitedTime);
        Long days = duration.toDays();
        assertTrue( duration.toDays() > 0);
        // rollback
        this.transactionManager.rollback(transaction);
    }

    @Test
    @Transactional
    @DisplayName("test lấy bill trước khi thanh toán của khách hàng có tên Phúc ")
    public void testGetAllBillBeforePayment_correct_name() {
        // Tạo dữ liệu thử nghiệm
        TransactionStatus transaction = this.transactionManager.getTransaction(null);
        initData();
        Page<BillBeforePaymentResponse> billResponses = billService.getAllBillBeforePayment(

                PageRequest.of(0, 1000000));

        // compare size bills in db not filter
        assertEquals(1, billResponses.getContent().size());
        List<BillBeforePaymentResponse> bills = billResponses.getContent();
        assertEquals(false, bills.get(0).getStatus());
        assertEquals("Phúc", bills.get(0).getApartment().getCustomerEntity().getName());
        // rollback
        this.transactionManager.rollback(transaction);
    }
    @Test
    @Transactional
    @DisplayName("test lấy bill trước khi thanh toán của Phúc tại phường văn quán ")
    public void testGetAllBillBeforePayment_correct_address() {
        // Tạo dữ liệu thử nghiệm
        TransactionStatus transaction = this.transactionManager.getTransaction(null);
        initData();
        Page<BillBeforePaymentResponse> billResponses = billService.getAllBillBeforePayment(

                PageRequest.of(0, 1000000));

        // compare size bills in db not filter
        assertEquals(1, billResponses.getContent().size());
        List<BillBeforePaymentResponse> bills = billResponses.getContent();
        assertEquals(false, bills.get(0).getStatus());
        assertEquals("Phúc", bills.get(0).getApartment().getCustomerEntity().getName());
        assertEquals("Van Quan", bills.get(0).getApartment().getAddressEntity().getWard());

        // rollback
        this.transactionManager.rollback(transaction);
    }
    @Test
    @Transactional
    @DisplayName("kiểm tra kết quả trả ra đúng như mong đợi, lấy được hạn đóng là 15-05-2024 ")
    public void testGetAllBillBeforePayment_correct_limitedTime() {
        // Tạo dữ liệu thử nghiệm
        TransactionStatus transaction = this.transactionManager.getTransaction(null);
        initData();
        Page<BillBeforePaymentResponse> billResponses = billService.getAllBillBeforePayment(

                PageRequest.of(0, 1000000));

        // compare size bills in db not filter
        assertEquals(1, billResponses.getContent().size());
        List<BillBeforePaymentResponse> bills = billResponses.getContent();
        assertEquals(false, bills.get(0).getStatus());
        // Kiểm tra đuúng ngay limitedTime
        assertEquals("15-05-2024",bills.get(0).getLimitedTimeResponse());


        // rollback
        this.transactionManager.rollback(transaction);
    }

    @Test
    @Transactional
    @DisplayName("Kiểm tra lấy thành công khách hàng đã thanh toán")
    public void testGetAllBillAfterPayment_correct() {
        // Tạo dữ liệu thử nghiệm
        TransactionStatus transaction = this.transactionManager.getTransaction(null);
        initData();
        Page<BillAfterPaymentResponse> billResponses = billService.getAllBillAfterPayment(

                PageRequest.of(0, 1000000),"", "");

        // compare size bills in db not filter
        assertEquals(1, billResponses.getContent().size());
        List<BillAfterPaymentResponse> bills = billResponses.getContent();
        assertEquals(true, bills.get(0).getStatus());
        // Kiểm tra đuúng ngay limitedTime
        assertEquals("15-05-2024",bills.get(0).getLimitedTimeResponse());


        // rollback
        this.transactionManager.rollback(transaction);
    }

    @Test
    @Transactional
    @DisplayName("Kiểm tra lấy thành công khách hàng đã thanh toán có ngày thanh toán trước ngày hạn")
    public void testGetAllBillAfterPayment_paybeforeLimitTime() {
        // Tạo dữ liệu thử nghiệm
        TransactionStatus transaction = this.transactionManager.getTransaction(null);
        initData();
        Page<BillAfterPaymentResponse> billResponses = billService.getAllBillAfterPayment(

                PageRequest.of(0, 1000000), "", "");

        // compare size bills in db not filter
        assertEquals(1, billResponses.getContent().size());
        List<BillAfterPaymentResponse> bills = billResponses.getContent();
        assertEquals(true, bills.get(0).getStatus());
        // Kiểm tra đuúng ngay limitedTime
        assertEquals("15-05-2024",bills.get(0).getLimitedTimeResponse());
        // Kiểm tra ngày hiện tại trừ limitedTime > 7
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy'T'HH:mm:ssXXX");

        // Phân tích chuỗi thành OffsetDateTime
        OffsetDateTime limitedTime = OffsetDateTime.parse(bills.get(0).getLimitedTimeResponse()+"T00:00:00+00:00", formatter);
        OffsetDateTime submitTime = OffsetDateTime.parse(bills.get(0).getSubmitTimeResponse()+"T00:00:00+00:00", formatter);
        Duration duration = Duration.between(limitedTime,submitTime);
        Long days = duration.toDays();
        assertFalse( duration.toDays() > 0);

        // rollback
        this.transactionManager.rollback(transaction);
    }

    @Test
    @Transactional
    @DisplayName("Kiểm tra xử lý ngoại lệ khi không tìm thấy dữ liệu khi tìm bill chưa thanh toán")
    public void testGetAllBillBeforePayment_data_not_found() {
        // Khởi tạo giao dịch
        TransactionStatus transaction = transactionManager.getTransaction(null);

        // Kiểm tra xem phương thức có ném ra ngoại lệ RuntimeException như mong đợi không
        assertThrows(RuntimeException.class, () -> {
            billService.getAllBillBeforePayment(PageRequest.of(0, 1000000));
        });

        // Rollback để đảm bảo dữ liệu không bị thay đổi
        transactionManager.rollback(transaction);
    }
    @Test
    @Transactional
    @DisplayName("Kiểm tra xử lý ngoại lệ khi không tìm thấy dữ liệu khi tìm bill đã thanh toán")
    public void testGetAllBillAfterPayment_data_not_found() {
        // Khởi tạo giao dịch
        TransactionStatus transaction = transactionManager.getTransaction(null);

        // Kiểm tra xem phương thức có ném ra ngoại lệ RuntimeException như mong đợi không
        assertThrows(RuntimeException.class, () -> {
            billService.getAllBillAfterPayment(PageRequest.of(0, 1000000), null,null);
        });

        // Rollback để đảm bảo dữ liệu không bị thay đổi
        transactionManager.rollback(transaction);
    }

    @Test
    @Transactional
    @DisplayName("Kiểm tra xử lý của dữ liệu khi không có dữ liệu apartment, address, hoặc customer tương ứng")
    public void testGetAllBillBeforePayment_missing_related_data() {
        // Khởi tạo giao dịch
        TransactionStatus transaction = transactionManager.getTransaction(null);

        // Mock các repository để trả về Optional rỗng khi gọi các phương thức findById
        when(apartmentRepository.findById(1L)).thenReturn(Optional.empty());
        when(addressRepository.findById(1L)).thenReturn(Optional.empty());
        when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        // Kiểm tra xem phương thức có ném ra ngoại lệ RuntimeException như mong đợi không
        assertThrows(RuntimeException.class, () -> {
            billService.getAllBillBeforePayment(PageRequest.of(0, 1000000));
        });

        // Rollback để đảm bảo dữ liệu không bị thay đổi
        transactionManager.rollback(transaction);
    }

    @Test
    @Transactional
    @DisplayName("Kiểm tra lấy thành công khách hàng quá hạn > 7 ngay`")
    public void testGetAllBillOverTime_paybeforeLimitTime() {
        // Tạo dữ liệu thử nghiệm
        TransactionStatus transaction = this.transactionManager.getTransaction(null);
        initData();
        Page<BillAfterPaymentResponse> billResponses = billService.getAllBillOverTime(

                PageRequest.of(0, 1000000),"", "");

        // compare size bills in db not filter
        assertEquals(1, billResponses.getContent().size());
        List<BillAfterPaymentResponse> bills = billResponses.getContent();
        assertEquals(false, bills.get(0).getStatus());
        // Kiểm tra đuúng ngay limitedTime
        assertEquals("04-05-2024",bills.get(0).getLimitedTimeResponse());
        // Kiểm tra ngày hiện tại trừ limitedTime >= 7
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy'T'HH:mm:ssXXX");

        // Phân tích chuỗi thành OffsetDateTime
        OffsetDateTime limitedTime = OffsetDateTime.parse(bills.get(0).getLimitedTimeResponse()+"T00:00:00+00:00", formatter);
        OffsetDateTime currentTime = OffsetDateTime.now();
        Duration duration = Duration.between(limitedTime,currentTime);
        Long days = duration.toDays();
        assertTrue( duration.toDays() > 7);

        // rollback
        this.transactionManager.rollback(transaction);
    }

    @Test
    @Transactional
    @DisplayName("Kiểm tra nếu khách hàng quá hạn 6 ngày sẽ không lấy được bill quá hạn")
    public void testGetAllBillOverTime_min6day() {
        // Tạo dữ liệu thử nghiệm
        TransactionStatus transaction = this.transactionManager.getTransaction(null);
        initData();
        Page<BillAfterPaymentResponse> billResponses = billService.getAllBillOverTime(

                PageRequest.of(0, 1000000),"", "");

        // compare size bills in db not filter
        assertEquals(1, billResponses.getContent().size());
        List<BillAfterPaymentResponse> bills = billResponses.getContent();
        assertEquals(false, bills.get(0).getStatus());
        // Kiểm tra đuúng ngay limitedTime
        assertEquals("04-05-2024",bills.get(0).getLimitedTimeResponse());
        // Kiểm tra ngày hiện tại trừ limitedTime >= 7
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy'T'HH:mm:ssXXX");

        // Phân tích chuỗi thành OffsetDateTime
        OffsetDateTime limitedTime = OffsetDateTime.parse(bills.get(0).getLimitedTimeResponse()+"T00:00:00+00:00", formatter);
        OffsetDateTime currentTime = OffsetDateTime.parse("10-05-2024"+"T00:00:00+00:00", formatter);
        Duration duration = Duration.between(limitedTime,currentTime);
        Long days = duration.toDays();
        assertTrue( duration.toDays() < 7);

        // rollback
        this.transactionManager.rollback(transaction);
    }

    @Test
    @Transactional
    @DisplayName("Kiểm tra lấy thành công khách hàng quá hạn = 7 ngay`")
    public void testGetAllBillOverTime_equalLimitTime() {
        // Tạo dữ liệu thử nghiệm
        TransactionStatus transaction = this.transactionManager.getTransaction(null);
        initData();
        Page<BillAfterPaymentResponse> billResponses = billService.getAllBillOverTime(

                PageRequest.of(0, 1000000), "", "");

        // compare size bills in db not filter
        assertEquals(1, billResponses.getContent().size());
        List<BillAfterPaymentResponse> bills = billResponses.getContent();
        assertEquals(false, bills.get(0).getStatus());
        // Kiểm tra đuúng ngay limitedTime
        assertEquals("04-05-2024",bills.get(0).getLimitedTimeResponse());
        // Kiểm tra ngày hiện tại trừ limitedTime >= 7
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy'T'HH:mm:ssXXX");

        // Phân tích chuỗi thành OffsetDateTime
        OffsetDateTime limitedTime = OffsetDateTime.parse(bills.get(0).getLimitedTimeResponse()+"T00:00:00+00:00", formatter);
        OffsetDateTime currentTime = OffsetDateTime.parse("11-05-2024"+"T00:00:00+00:00", formatter);
        long daysDifference = ChronoUnit.DAYS.between(limitedTime, currentTime);
        assertEquals(7, daysDifference);

        // rollback
        this.transactionManager.rollback(transaction);
    }


}
