package com.example.electricitybillingsystem.service;

import com.example.electricitybillingsystem.model.*;
import com.example.electricitybillingsystem.repository.*;
import com.example.electricitybillingsystem.vo.request.TurnOffWaterInfoRequest;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class EmailServiceTest {
    @Mock
    private CustomerService customerService;

    @InjectMocks
    private EmailService emailService;

    @Mock
    private JavaMailSender mailSender; // Mocking JavaMailSender

    @Autowired
    private AddressRepository addressRepository;

    @Mock
    private AddressService addressService;
    @Autowired
    private BillRepository billRepository;
    @Autowired
    private CustomerRepository customerRepository;

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
                .apartmentId(1L)
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
                .id(1L)
                .name("Phúc")
                .build();
        customerRepository.save(customerPhuc);
        ApartmentEntity apartmentPhuc = ApartmentEntity.builder()
                .id(1L)
                .customerId(1L)
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
                .apartmentId(2L)
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
                .id(2L)
                .name("Phú")
                .build();
        customerRepository.save(customerPhu);
        ApartmentEntity apartmentPhu = ApartmentEntity.builder()
                .id(2L)
                .customerId(2L)
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
                .apartmentId(3L)
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
                .id(3L)
                .name("Ngọc")
                .build();
        customerRepository.save(customerNgoc);
        ApartmentEntity apartmentNgoc = ApartmentEntity.builder()
                .id(3L)
                .customerId(3L)
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

    @BeforeEach
    void setup() {

    }

    @Test
    @DisplayName("Kiểm tra gửi email tới cho người dùng")
    @MockitoSettings(strictness = Strictness.LENIENT)
    public void SendEmail_success() throws MessagingException, UnsupportedEncodingException {
        // Mock data
        String email = "test@gmail.com";
        String subject = "Test Subject";
        String content = "Test Content";

        // Tạo mock object cho MimeMessage
        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        // Gọi phương thức cần test
        boolean result = emailService.sendEmail(email, subject, content, mimeMessage);
        verify(mailSender, times(0)).createMimeMessage();

        assertTrue(result);
    }


    @Test
    public void testSendEmailBeforePayment() throws Exception {
        // Mock data
        List<Long> billIds = Arrays.asList(1L, 2L);
        CustomerEntity customer1 = new CustomerEntity(1L, "John Doe", OffsetDateTime.now(), "123456789", "Note", "john@example.com");
        CustomerEntity customer2 = new CustomerEntity(2L, "Jane Doe", OffsetDateTime.now(), "987654321", "Another note", "jane@example.com");
        List<CustomerEntity> allCustomers = Arrays.asList(customer1, customer2);

        BillEntity bill1 = new BillEntity(1L, OffsetDateTime.now(), OffsetDateTime.now().plusDays(30), "Description", "ABC123", BigDecimal.valueOf(100), true, "Paid", 100L, 200L, 1L, 1L, 1L, 5, 2024);
        BillEntity bill2 = new BillEntity(2L, OffsetDateTime.now(), OffsetDateTime.now().plusDays(30), "Description", "DEF456", BigDecimal.valueOf(200), true, "Paid", 200L, 300L, 2L, 2L, 2L, 5, 2024);
        Map<Long, BillEntity> billMap = Map.of(1L, bill1, 2L, bill2);

        // Stubbing the behavior of customerService
        when(customerService.getAllCustomerByBillIds(billIds)).thenReturn(allCustomers);
        when(customerService.getBillMap(billIds)).thenReturn(billMap);

        // Stubbing the behavior of mailSender
        doNothing().when(mailSender).send(any(MimeMessage.class));

        // Perform the method under test
        String result = emailService.sendEmailBeforPayment(billIds);

        // Verify the interactions
        verify(customerService, times(1)).getAllCustomerByBillIds(billIds);
        verify(customerService, times(1)).getBillMap(billIds);
        verify(mailSender, times(2)).send(any(MimeMessage.class));

        // Assert the result
        assertEquals("Email sent successfully", result);
    }


    @Nested
    @Transactional
    @DisplayName("Kiểm tra gửi email khi có thông báo cắt nước")
    class sendEmailTurnOffWater {
        @Test
        @DisplayName("Ngày bắt đầu và ngày kết thúc là string rỗng")
        void case1() throws MessagingException, UnsupportedEncodingException {
            TurnOffWaterInfoRequest turnOffWaterInfoRequest = new TurnOffWaterInfoRequest();
            turnOffWaterInfoRequest.setEndTime("");
            turnOffWaterInfoRequest.setStartTime("");

            String res = emailService.sendEmailTurnOffWater(turnOffWaterInfoRequest);
            assertEquals("Bạn phải chọn thời gian bắt đầu và kết thúc!", res);
        }

        @Test
        @DisplayName("Ngày bắt đầu là string rỗng và ngày kết thúc khác rỗng")
        void case2() throws MessagingException, UnsupportedEncodingException {
            TurnOffWaterInfoRequest turnOffWaterInfoRequest = new TurnOffWaterInfoRequest();
            turnOffWaterInfoRequest.setStartTime("");
            turnOffWaterInfoRequest.setEndTime("2024-05-13");

            String res = emailService.sendEmailTurnOffWater(turnOffWaterInfoRequest);
            assertEquals("Bạn phải chọn thời gian bắt đầu và kết thúc!", res);
        }


        @Test
        @DisplayName("Ngày kết thúc là string rỗng và ngày bắt đầu khác rỗng")
        void case3() throws MessagingException, UnsupportedEncodingException {
            TurnOffWaterInfoRequest turnOffWaterInfoRequest = new TurnOffWaterInfoRequest();
            turnOffWaterInfoRequest.setEndTime("");
            turnOffWaterInfoRequest.setStartTime("2024-05-13");

            String res = emailService.sendEmailTurnOffWater(turnOffWaterInfoRequest);
            assertEquals("Bạn phải chọn thời gian bắt đầu và kết thúc!", res);
        }

        @Test
        @DisplayName("Ngày kết thúc và ngày bắt đầu khác rỗng và giá trị phường là null")
        void case4() throws MessagingException, UnsupportedEncodingException {
            TurnOffWaterInfoRequest turnOffWaterInfoRequest = new TurnOffWaterInfoRequest();
            turnOffWaterInfoRequest.setEndTime("2024-05-14");
            turnOffWaterInfoRequest.setStartTime("2024-05-13");

            String res = emailService.sendEmailTurnOffWater(turnOffWaterInfoRequest);
            assertEquals("Yêu cầu chọn phường!", res);
        }


        @Test
        @DisplayName("Không tồn tại thông tin khách hàng")
        void case5() throws MessagingException, UnsupportedEncodingException {
            TransactionStatus transaction = transactionManager.getTransaction(null);

            TurnOffWaterInfoRequest turnOffWaterInfoRequest = new TurnOffWaterInfoRequest();
            turnOffWaterInfoRequest.setEndTime("2024-05-14");
            turnOffWaterInfoRequest.setStartTime("2024-05-13");
            turnOffWaterInfoRequest.setWard("Mo Lao");

            addressRepository.deleteAll();

            String res = emailService.sendEmailTurnOffWater(turnOffWaterInfoRequest);
            assertEquals("Không tồn tại khách hàng nào", res);

            transactionManager.rollback(transaction);
        }

        @Test
        @DisplayName("Tồn tại thông tin khách hàng")
        void case6() throws MessagingException, UnsupportedEncodingException {
            TransactionStatus transaction = transactionManager.getTransaction(null);
            TurnOffWaterInfoRequest turnOffWaterInfoRequest = new TurnOffWaterInfoRequest();
            turnOffWaterInfoRequest.setEndTime("2024-05-14");
            turnOffWaterInfoRequest.setStartTime("2024-05-13");
            turnOffWaterInfoRequest.setWard("Van Quan");

            List<CustomerEntity> customers = new ArrayList<>();
            customers.add(CustomerEntity
                    .builder()
                    .id(1L)
                    .name("customer")
                    .email("customer@gmail.com")
                    .phone("0123456")
                    .build());

            when(addressService.getAllCustomerByWard(turnOffWaterInfoRequest.getWard())).thenReturn(customers);
            System.out.println("customers::");
            System.out.println(customers.size());
//            assertNotEquals(0,customers.size());
            verify(addressService, times(1));

            String res = emailService.sendEmailTurnOffWater(turnOffWaterInfoRequest);
            assertEquals("Email sent successfully",res);

            transactionManager.rollback(transaction);
        }
    }
}
