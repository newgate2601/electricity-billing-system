package com.example.electricitybillingsystem.controller;

import com.example.electricitybillingsystem.model.CustomerEntity;
import com.example.electricitybillingsystem.service.*;
import com.example.electricitybillingsystem.vo.request.TurnOffWaterInfoRequest;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class SendEmailController {

    private final EmailService emailService;
    private final CustomerService customerService;
    private final TaxService taxService;
    private final TieredPricingService tieredPricingService;

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

    @Operation(summary = "notification turn off water")
    @PostMapping("/sendEmail-turnOffWater")
    public ResponseEntity<String> sendEmailTurnOffWater(@RequestBody TurnOffWaterInfoRequest turnOffWaterInfoRequest) {
        String startTime = turnOffWaterInfoRequest.getStartTime();
        String endTime = turnOffWaterInfoRequest.getEndTime();
        if (endTime.equals("") || startTime.equals("")) {
            return ResponseEntity.ok("Bạn phải chọn thời gian bắt đầu và kết thúc!");
        }

        List<CustomerEntity> customers = emailService.sendEmailTurnOffWater(turnOffWaterInfoRequest.getWard());
        if (customers == null || customers.size() == 0) {
            return ResponseEntity.ok("Does not exist customer");
        }

        List<String> customerEmail = customers.stream().map(CustomerEntity::getEmail).collect(Collectors.toList());
        for (String email : customerEmail) {
            sendMailToCustomer(email, "Thông báo cắt nước", "<p>Thời gian tới sẽ cắt nước trong khoảng thời gian từ:</p> <p>" + startTime + " tới " + endTime + "</p>");
        }
        return ResponseEntity.ok("Email sent successfully");
    }


    @Operation(summary = "notification adjust price water")
    @PostMapping("/sendEmail-adjustPrice")
    public ResponseEntity<String> sendEmailAdjustPriceWater(@RequestBody Map<String, String> request) {
        List<CustomerEntity> allCustomer = customerService.getAllCustomer();
        if (allCustomer == null) {
            return ResponseEntity.ok("Does not exist customer");
        }
        List<String> customerEmail = allCustomer.stream().map(CustomerEntity::getEmail).collect(Collectors.toList());
        for (String email : customerEmail) {
            sendMailToCustomer(email, "Thông báo điều chỉnh giá: ", request.get("content"));
        }
        taxService.updateAllStatusToFalse();
        tieredPricingService.updateAllStatusToFalse();
        return ResponseEntity.ok("Email sent successfully");

    }


    private void sendMailToCustomer(String email, String subject, String content) {
        try {
            emailService.sendEmail(email, subject, content);
        } catch (MessagingException | UnsupportedEncodingException e) {
            System.out.println("[Email-" + email + "] - Error::" + e.getMessage());
        }
    }

}
