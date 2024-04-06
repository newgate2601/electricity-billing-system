package com.example.electricitybillingsystem.controllers;

import com.example.electricitybillingsystem.models.Bill;
import com.example.electricitybillingsystem.services.BillService;
import com.example.electricitybillingsystem.services.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class SendEmailController {

    private final EmailService emailService;
    private final BillService billService;

    @PostMapping("/sendEmail")
    public String sendEmail(@RequestBody Map<String, String> request) {
        String email = request.get("email");

        // Call the sendEmail method to send an email
        String subject = "Hello from Spring Boot";
        String content = "<p>Hello,</p><p>Phúc em.</p>";

        try {
            emailService.sendEmail(email, subject, content);
            return "Email sent successfully.";
        } catch (MessagingException | UnsupportedEncodingException e) {
            return "Failed to send email. Error: " + e.getMessage();
        }
    }

    @GetMapping("/getAllBill")
    public List<Bill> getBill() {
        return billService.getAllBill();
    }
}
