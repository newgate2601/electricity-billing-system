package com.example.electricitybillingsystem.service;

import com.example.electricitybillingsystem.model.*;
import com.example.electricitybillingsystem.repository.*;
import com.example.electricitybillingsystem.vo.request.TurnOffWaterInfoRequest;
import jakarta.mail.Address;
import jakarta.mail.MessagingException;
import jakarta.mail.Multipart;
import jakarta.mail.internet.MimeMessage;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
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

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class EmailServiceTest {
    @Mock
    private CustomerService customerService;

    @Mock
    private AddressService addressService;
    @Mock
    private JavaMailSender mailSender; // Mocking JavaMailSender
    @InjectMocks
    private EmailService emailService;


    @Autowired
    private AddressRepository addressRepository;

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
        MockitoAnnotations.initMocks(this);
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
    @DisplayName("Kiểm Tra gửi thông báo cắt nước thành công tới khách hàng ")
    public void testSendEmailTurnOffWater_success() throws MessagingException, UnsupportedEncodingException {
        // Arrange
        TurnOffWaterInfoRequest request = new TurnOffWaterInfoRequest();
        request.setStartTime("10:00");
        request.setEndTime("12:00");
        request.setWard("Ward1");

        // Tạo danh sách khách hàng mẫu
        CustomerEntity customerEntity1 = new CustomerEntity();
        customerEntity1.setEmail("ngoc@gmail.com");
        CustomerEntity customerEntity2 = new CustomerEntity();
        customerEntity2.setEmail("ngoc2@gmail.com");

        List<CustomerEntity> customers = Arrays.asList(customerEntity1, customerEntity2);

        // Tạo mock cho addressService
        AddressService mockAddressService = mock(AddressService.class);
        when(mockAddressService.getAllCustomerByWard(anyString())).thenReturn(customers);

        // Tạo mock cho mailSender
        JavaMailSender mailSender = mock(JavaMailSender.class);
        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        // Tạo emailService với mockAddressService và mailSender được inject
        EmailService emailService = new EmailService(mailSender, mockAddressService, mock(CustomerService.class), mock(TaxService.class), mock(TieredPricingService.class));

        // Act
        String result = emailService.sendEmailTurnOffWater(request);

        // Assert
        assertEquals("Email sent successfully", result);
        verify(mimeMessage, times(customers.size())).setContent(any(Multipart.class)); // Kiểm tra rằng setContent được gọi đúng số lần tương ứng với số lượng khách hàng
    }

    @Test
    @DisplayName("Kiểm Tra gửi thông báo cắt nước khi  không có khách hàng sẽ thông báo không tìm thấy khách hàng")
    public void testSendEmailTurnOffWater_fail_nocustomer() throws MessagingException, UnsupportedEncodingException {
        TurnOffWaterInfoRequest request = new TurnOffWaterInfoRequest();
        request.setStartTime("10:00");
        request.setEndTime("12:00");
        request.setWard("Ward1");

        // Tạo danh sách khách hàng rỗng
        List<CustomerEntity> emptyCustomers = new ArrayList<>();

        // Tạo mock cho addressService và thiết lập nó để trả về danh sách khách hàng rỗng
        AddressService mockAddressService = mock(AddressService.class);
        when(mockAddressService.getAllCustomerByWard(anyString())).thenReturn(emptyCustomers);

        // Tạo emailService với mockAddressService và các mock khác
        EmailService emailService = new EmailService(mock(JavaMailSender.class), mockAddressService, mock(CustomerService.class), mock(TaxService.class), mock(TieredPricingService.class));

        // Act
        String result = emailService.sendEmailTurnOffWater(request);

        // Assert
        assertEquals("Không tồn tại khách hàng nào", result);
    }

    @Test
    @DisplayName("Kiểm Tra chức năng thông báo cắt nước khi  không nhập thông tin giờ cắt nước")
    public void testSendEmailTurnOffWater_fail_time() throws MessagingException, UnsupportedEncodingException {
        TurnOffWaterInfoRequest request = new TurnOffWaterInfoRequest();
        request.setStartTime("");
        request.setEndTime("");
        request.setWard("Ward1");
        // Tạo emailService với mockAddressService và các mock khác
        EmailService emailService = new EmailService(mock(JavaMailSender.class), mock(AddressService.class), mock(CustomerService.class), mock(TaxService.class), mock(TieredPricingService.class));

        // Act
        String result = emailService.sendEmailTurnOffWater(request);

        // Assert
        assertEquals("Bạn phải chọn thời gian bắt đầu và kết thúc!", result);
    }
    @Test
    @DisplayName("Kiểm Tra gửi email trước khi thanh toán với tồn tại khách hàng")
    public void testSendEmailBeforePayment_customerExists() throws MessagingException, UnsupportedEncodingException {
        // Arrange
        JavaMailSender mockMailSender = mock(JavaMailSender.class);
        AddressService mockAddressService = mock(AddressService.class);
        CustomerService mockCustomerService = mock(CustomerService.class);
        TaxService mockTaxService = mock(TaxService.class);
        TieredPricingService mockTieredPricingService = mock(TieredPricingService.class);

        EmailService emailService = new EmailService(mockMailSender, mockAddressService, mockCustomerService, mockTaxService, mockTieredPricingService);

        List<Long> billIds = new ArrayList<>(); // Danh sách billIds mẫu
        CustomerEntity customerEntity = new CustomerEntity();
        customerEntity.setId(1L);
        customerEntity.setName("Phuc");
        customerEntity.setEmail("example@example.com");
        List<CustomerEntity> customers = Collections.singletonList(customerEntity);

        // Tạo mock cho longBillEntityMap để không trả về null
        Map<Long, BillEntity> longBillEntityMap = new HashMap<>();
        BillEntity billEntity = new BillEntity();
        billEntity.setPrice(BigDecimal.valueOf(100)); // Thiết lập giá trị cho billEntity để tránh lỗi NullPointerException
        longBillEntityMap.put(customerEntity.getId(), billEntity);

        // Mock customerService.getAllCustomerByBillIds(billIds) để trả về danh sách khách hàng
        when(mockCustomerService.getAllCustomerByBillIds(billIds)).thenReturn(customers);
        // Mock customerService.getBillMap(billIds) để trả về longBillEntityMap
        when(mockCustomerService.getBillMap(billIds)).thenReturn(longBillEntityMap);

        // Mock JavaMailSender để trả về mimeMessage không null khi gọi createMimeMessage()
        MimeMessage mockMimeMessage = mock(MimeMessage.class);
        when(mockMailSender.createMimeMessage()).thenReturn(mockMimeMessage);

        // Act
        String result = emailService.sendEmailBeforPayment(billIds);

        // Assert
        assertEquals("Email sent successfully", result); // Kiểm tra kết quả trả về của phương thức
        verify(mockCustomerService, times(1)).getAllCustomerByBillIds(billIds); // Xác nhận rằng phương thức getAllCustomerByBillIds đã được gọi đúng một lần
        verify(mockMailSender, times(1)).send(any(MimeMessage.class)); // Xác nhận rằng phương thức send của mockMailSender đã được gọi đúng một lần
    }

    @Test
    @DisplayName("Kiểm Tra gửi email trước khi thanh toán mà không có khách hàng nào")
    public void testSendEmailBeforePayment_noCustomer() throws MessagingException, UnsupportedEncodingException {
        // Arrange
        JavaMailSender mockMailSender = mock(JavaMailSender.class);
        AddressService mockAddressService = mock(AddressService.class);
        CustomerService mockCustomerService = mock(CustomerService.class);
        TaxService mockTaxService = mock(TaxService.class);
        TieredPricingService mockTieredPricingService = mock(TieredPricingService.class);

        EmailService emailService = new EmailService(mockMailSender, mockAddressService, mockCustomerService, mockTaxService, mockTieredPricingService);

        List<Long> billIds = new ArrayList<>(); // Danh sách billIds mẫu

        // Mock customerService.getAllCustomerByBillIds(billIds) để trả về null
        when(mockCustomerService.getAllCustomerByBillIds(billIds)).thenReturn(null);

        // Act
        String result = emailService.sendEmailBeforPayment(billIds);

        // Assert
        assertEquals("Không tồn tại khách hàng nào", result); // Kiểm tra kết quả trả về của phương thức
        verify(mockCustomerService, times(1)).getAllCustomerByBillIds(billIds); // Xác nhận rằng phương thức getAllCustomerByBillIds đã được gọi đúng một lần
        verifyNoInteractions(mockMailSender); // Xác nhận rằng không có phương thức nào của mockMailSender đã được gọi
    }

    @Test
    @DisplayName("Kiểm Tra gửi email sau khi thanh toán với tồn tại khách hàng")
    public void testSendEmailAfterPayment_customerExists() throws MessagingException, UnsupportedEncodingException {
        // Arrange
        JavaMailSender mockMailSender = mock(JavaMailSender.class);
        AddressService mockAddressService = mock(AddressService.class);
        CustomerService mockCustomerService = mock(CustomerService.class);
        TaxService mockTaxService = mock(TaxService.class);
        TieredPricingService mockTieredPricingService = mock(TieredPricingService.class);

        EmailService emailService = new EmailService(mockMailSender, mockAddressService, mockCustomerService, mockTaxService, mockTieredPricingService);

        List<Long> billIds = new ArrayList<>(); // Danh sách billIds mẫu
        CustomerEntity customerEntity = new CustomerEntity();
        customerEntity.setId(1L);
        customerEntity.setName("Phuc");
        customerEntity.setEmail("example@example.com");
        List<CustomerEntity> customers = Collections.singletonList(customerEntity);

        // Tạo mock cho longBillEntityMap để không trả về null
        Map<Long, BillEntity> longBillEntityMap = new HashMap<>();
        BillEntity billEntity = new BillEntity();
        billEntity.setPrice(BigDecimal.valueOf(100)); // Thiết lập giá trị cho billEntity để tránh lỗi NullPointerException
        longBillEntityMap.put(customerEntity.getId(), billEntity);

        // Mock customerService.getAllCustomerByBillIds(billIds) để trả về danh sách khách hàng
        when(mockCustomerService.getAllCustomerByBillIds(billIds)).thenReturn(customers);
        // Mock customerService.getBillMap(billIds) để trả về longBillEntityMap
        when(mockCustomerService.getBillMap(billIds)).thenReturn(longBillEntityMap);

        // Mock JavaMailSender để trả về mimeMessage không null khi gọi createMimeMessage()
        MimeMessage mockMimeMessage = mock(MimeMessage.class);
        when(mockMailSender.createMimeMessage()).thenReturn(mockMimeMessage);

        // Act
        String result = emailService.sendEmailAfterPayment(billIds);

        // Assert
        assertEquals("Email sent successfully", result); // Kiểm tra kết quả trả về của phương thức
        verify(mockCustomerService, times(1)).getAllCustomerByBillIds(billIds); // Xác nhận rằng phương thức getAllCustomerByBillIds đã được gọi đúng một lần
        verify(mockMailSender, times(1)).send(any(MimeMessage.class)); // Xác nhận rằng phương thức send của mockMailSender đã được gọi đúng một lần
    }

    @Test
    @DisplayName("Kiểm Tra gửi email trước khi thanh toán mà không có khách hàng nào")
    public void testSendEmailAfterPayment_noCustomer() throws MessagingException, UnsupportedEncodingException {
        // Arrange
        JavaMailSender mockMailSender = mock(JavaMailSender.class);
        AddressService mockAddressService = mock(AddressService.class);
        CustomerService mockCustomerService = mock(CustomerService.class);
        TaxService mockTaxService = mock(TaxService.class);
        TieredPricingService mockTieredPricingService = mock(TieredPricingService.class);

        EmailService emailService = new EmailService(mockMailSender, mockAddressService, mockCustomerService, mockTaxService, mockTieredPricingService);

        List<Long> billIds = new ArrayList<>(); // Danh sách billIds mẫu

        // Mock customerService.getAllCustomerByBillIds(billIds) để trả về null
        when(mockCustomerService.getAllCustomerByBillIds(billIds)).thenReturn(null);

        // Act
        String result = emailService.sendEmailAfterPayment(billIds);

        // Assert
        assertEquals("Không tồn tại khách hàng nào", result); // Kiểm tra kết quả trả về của phương thức
        verify(mockCustomerService, times(1)).getAllCustomerByBillIds(billIds); // Xác nhận rằng phương thức getAllCustomerByBillIds đã được gọi đúng một lần
        verifyNoInteractions(mockMailSender); // Xác nhận rằng không có phương thức nào của mockMailSender đã được gọi
    }

    @Test
    @DisplayName("Kiểm Tra gửi email quá hạn thanh toán mà không có khách hàng nào")
    public void testSendEmailOverTime_noCustomer() throws MessagingException, UnsupportedEncodingException {
        // Arrange
        JavaMailSender mockMailSender = mock(JavaMailSender.class);
        AddressService mockAddressService = mock(AddressService.class);
        CustomerService mockCustomerService = mock(CustomerService.class);
        TaxService mockTaxService = mock(TaxService.class);
        TieredPricingService mockTieredPricingService = mock(TieredPricingService.class);

        EmailService emailService = new EmailService(mockMailSender, mockAddressService, mockCustomerService, mockTaxService, mockTieredPricingService);

        List<Long> billIds = new ArrayList<>(); // Danh sách billIds mẫu

        // Mock customerService.getAllCustomerByBillIds(billIds) để trả về null
        when(mockCustomerService.getAllCustomerByBillIds(billIds)).thenReturn(null);

        // Act
        String result = emailService.sendEmailOverTime(billIds);

        // Assert
        assertEquals("Không tồn tại khách hàng nào", result); // Kiểm tra kết quả trả về của phương thức
        verify(mockCustomerService, times(1)).getAllCustomerByBillIds(billIds); // Xác nhận rằng phương thức getAllCustomerByBillIds đã được gọi đúng một lần
        verifyNoInteractions(mockMailSender); // Xác nhận rằng không có phương thức nào của mockMailSender đã được gọi
    }

    @Test
    @DisplayName("Kiểm Tra gửi email sau khi thanh toán với tồn tại khách hàng")
    public void testSendEmailOverTime_customerExists() throws MessagingException, UnsupportedEncodingException {
        // Arrange
        JavaMailSender mockMailSender = mock(JavaMailSender.class);
        AddressService mockAddressService = mock(AddressService.class);
        CustomerService mockCustomerService = mock(CustomerService.class);
        TaxService mockTaxService = mock(TaxService.class);
        TieredPricingService mockTieredPricingService = mock(TieredPricingService.class);

        EmailService emailService = new EmailService(mockMailSender, mockAddressService, mockCustomerService, mockTaxService, mockTieredPricingService);

        List<Long> billIds = new ArrayList<>(); // Danh sách billIds mẫu
        CustomerEntity customerEntity = new CustomerEntity();
        customerEntity.setId(1L);
        customerEntity.setName("Phuc");
        customerEntity.setEmail("example@example.com");
        List<CustomerEntity> customers = Collections.singletonList(customerEntity);

        // Tạo mock cho longBillEntityMap để không trả về null
        Map<Long, BillEntity> longBillEntityMap = new HashMap<>();
        BillEntity billEntity = new BillEntity();
        billEntity.setPrice(BigDecimal.valueOf(100)); // Thiết lập giá trị cho billEntity để tránh lỗi NullPointerException
        longBillEntityMap.put(customerEntity.getId(), billEntity);

        // Mock customerService.getAllCustomerByBillIds(billIds) để trả về danh sách khách hàng
        when(mockCustomerService.getAllCustomerByBillIds(billIds)).thenReturn(customers);
        // Mock customerService.getBillMap(billIds) để trả về longBillEntityMap
        when(mockCustomerService.getBillMap(billIds)).thenReturn(longBillEntityMap);

        // Mock JavaMailSender để trả về mimeMessage không null khi gọi createMimeMessage()
        MimeMessage mockMimeMessage = mock(MimeMessage.class);
        when(mockMailSender.createMimeMessage()).thenReturn(mockMimeMessage);

        // Act
        String result = emailService.sendEmailOverTime(billIds);

        // Assert
        assertEquals("Email sent successfully", result); // Kiểm tra kết quả trả về của phương thức
        verify(mockCustomerService, times(1)).getAllCustomerByBillIds(billIds); // Xác nhận rằng phương thức getAllCustomerByBillIds đã được gọi đúng một lần
        verify(mockMailSender, times(1)).send(any(MimeMessage.class)); // Xác nhận rằng phương thức send của mockMailSender đã được gọi đúng một lần
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
