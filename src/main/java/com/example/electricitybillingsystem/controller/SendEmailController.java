package com.example.electricitybillingsystem.controller;

import com.example.electricitybillingsystem.model.BillEntity;
import com.example.electricitybillingsystem.model.CustomerEntity;
import com.example.electricitybillingsystem.service.CustomerService;
import com.example.electricitybillingsystem.service.EmailService;
import com.example.electricitybillingsystem.service.TaxService;
import com.example.electricitybillingsystem.service.TieredPricingService;
import com.example.electricitybillingsystem.vo.request.TurnOffWaterInfoRequest;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class SendEmailController {

    private final EmailService emailService;
    private final CustomerService customerService;
    private final TaxService taxService;
    private final TieredPricingService tieredPricingService;


    @Operation(summary = "notification turn off water")
    @PostMapping("/sendEmail-turnOffWater")
    public ResponseEntity<String> sendEmailTurnOffWater(@RequestBody TurnOffWaterInfoRequest turnOffWaterInfoRequest) throws MessagingException, UnsupportedEncodingException {
      return ResponseEntity.ok(emailService.sendEmailTurnOffWater(turnOffWaterInfoRequest));
    }


    @Operation(summary = "notification adjust price water")
    @PostMapping("/sendEmail-adjustPrice")
    public ResponseEntity<String> sendEmailAdjustPriceWater(@RequestBody Map<String, String> request) throws MessagingException, UnsupportedEncodingException {
        return ResponseEntity.ok(emailService.sendEmailAdjustWater(request));
    }

    @Operation(summary = "notification send email before payment")
    @PostMapping("/sendEmail-beforepayment")
    public ResponseEntity<String> sendEmailBeforePaymenPriceWater(
            @RequestParam List<Long> billIds) throws MessagingException, UnsupportedEncodingException {
     return ResponseEntity.ok(emailService.sendEmailBeforPayment(billIds));
    }

    @Operation(summary = "send email overtime")
    @PostMapping("/sendEmail-overtime")
    public ResponseEntity<String> sendEmailOverTime(
            @RequestParam List<Long> billIds) throws MessagingException, UnsupportedEncodingException {
        return ResponseEntity.ok(emailService.sendEmailOverTime(billIds));
    }
    @Operation(summary = "sendemailafterpaymenr")
    @PostMapping("/sendEmail-after")
    public ResponseEntity<String> sendEmailAfterPaymenPriceWater(
            @RequestParam List<Long> billIds) throws MessagingException, UnsupportedEncodingException {
        return ResponseEntity.ok(emailService.sendEmailAfterPayment(billIds));

    }

    private void sendMailToCustomer(String email, String subject, String content) {
        try {
            emailService.sendEmail(email, subject, content);
        } catch (Exception e) {
            System.out.println("[Email-" + email + "] - Error::" + e.getMessage());
        }
    }

}
