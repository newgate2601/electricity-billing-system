package com.example.electricitybillingsystem.service;

import com.example.electricitybillingsystem.model.BillEntity;
import com.example.electricitybillingsystem.model.CustomerEntity;
import jakarta.mail.MessagingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
public class EmailServiceTest {
    @Mock
    private CustomerService customerService;

    @InjectMocks
    private EmailService emailService;

    @Test
    void sendEmailBeforePayment_Success() throws MessagingException, UnsupportedEncodingException {
        // Given
        List<Long> billIds = Arrays.asList(1L, 2L);

        // Mocking customer data
        CustomerEntity customer1 = new CustomerEntity(1L, "John", OffsetDateTime.now(), "123456789", "Regular customer", "john@example.com");
        CustomerEntity customer2 = new CustomerEntity(2L, "Jane", OffsetDateTime.now(), "987654321", "VIP customer", "jane@example.com");
        doReturn(Arrays.asList(customer1, customer2)).when(customerService).getAllCustomerByBillIds(billIds);

        // Mocking bill data
        Map<Long, BillEntity> billMap = Map.of(
                1L, new BillEntity(1L, OffsetDateTime.now(), OffsetDateTime.now().plusDays(7), "Bill for water", "ABC123", BigDecimal.valueOf(100.0), true, "Paid", 50L, 100L, 1L, 1L, 1L, 5, 2024),
                2L, new BillEntity(2L, OffsetDateTime.now(), OffsetDateTime.now().plusDays(7), "Bill for electricity", "DEF456", BigDecimal.valueOf(200.0), true, "Paid", 75L, 150L, 2L, 2L, 2L, 5, 2024)
        );
        doReturn(billMap).when(customerService).getBillMap(billIds);

        // Mocking sendEmail method
        doNothing().when(emailService).sendEmail(anyString(), anyString(), anyString());

        // When
        String result = emailService.sendEmailBeforPayment(billIds);

        // Then
        verify(customerService).getAllCustomerByBillIds(billIds);
        verify(customerService).getBillMap(billIds);
        verify(emailService, times(2)).sendEmail(anyString(), anyString(), anyString());
        assertEquals("Email sent successfully", result);
    }
}
