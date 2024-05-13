package com.example.electricitybillingsystem.service;

import com.example.electricitybillingsystem.model.BillEntity;
import com.example.electricitybillingsystem.model.CustomerEntity;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.shadow.com.univocity.parsers.annotations.Nested;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

    @Test
    @Nested()
    public void SendEmail_success() throws MessagingException, UnsupportedEncodingException {
        // Mock data
        String email = "test@example.com";
        String subject = "Test Subject";
        String content = "Test Content";

        // Mock MimeMessage
        MimeMessage mimeMessage = mock(MimeMessage.class);

        // Perform the method under test
        emailService.sendEmail(email, subject, content);

        // Verify the interactions
        verify(mailSender, times(1)).createMimeMessage(); // Verify that createMimeMessage() is called once
        verify(mailSender, times(1)).send(mimeMessage); // Verify that send() is called once

        // You can add more assertions if needed
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

}
