package com.example.electricitybillingsystem.controller;

import com.example.electricitybillingsystem.model.BillEntity;
import com.example.electricitybillingsystem.model.CustomerEntity;
import com.example.electricitybillingsystem.service.CustomerService;
import com.example.electricitybillingsystem.service.EmailService;
import com.example.electricitybillingsystem.service.TaxService;
import com.example.electricitybillingsystem.service.TieredPricingService;
import com.example.electricitybillingsystem.vo.request.TurnOffWaterInfoRequest;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/sendEmail")
    public String sendEmail() {
        String email = "pphuc9122002@gmail.com";

        // Call the sendEmail method to send an email
        String subject = "Hello from Spring Boot";
        String content = "<p>Hello,</p><p>Phúc em.</p>";

        try {
            emailService.sendEmail(email, subject, content);
            return "Email sent successfully.";
        } catch (Exception e) {
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

    @Operation(summary = "notification send email before payment")
    @PostMapping("/sendEmail-beforepayment")
    public ResponseEntity<String> sendEmailBeforePaymenPriceWater(
            @RequestParam List<Long> billIds) {

        List<CustomerEntity> allCustomer = customerService.getAllCustomerByBillIds(billIds);
        Map<Long, CustomerEntity> customerEntityMap = allCustomer.stream().collect(Collectors.toMap(
                CustomerEntity::getId, Function.identity()));
        Map<Long, BillEntity> longBillEntityMap = customerService.getBillMap(billIds);
        if (allCustomer == null) {
            return ResponseEntity.ok("Does not exist customer");
        }
        List<String> customerEmail = allCustomer.stream().map(CustomerEntity::getEmail).collect(Collectors.toList());
        for (CustomerEntity customerEntity : allCustomer) {
            String email = customerEntity.getEmail();
            BillEntity billEntity = longBillEntityMap.get(customerEntity.getId());
            sendMailToCustomer(email, "Thông báo hóa đơn cần đóng ",
                    "<p>Hóa đơn của khách hàng cần thanh toán: :</p> <p>" + billEntity.getPrice()
                            + "số nước cũ: " + billEntity.getStartNumber() +
                            "số nước mới: " + billEntity.getEndNumber() + "</p>");
        }
        return ResponseEntity.ok("Email sent successfully");

    }

    @Operation(summary = "send email overtime")
    @PostMapping("/sendEmail-overtime")
    public ResponseEntity<String> sendEmailOverTime(
            @RequestParam List<Long> billIds) {

        List<CustomerEntity> allCustomer = customerService.getAllCustomerByBillIds(billIds);
        Map<Long, CustomerEntity> customerEntityMap = allCustomer.stream().collect(Collectors.toMap(
                CustomerEntity::getId, Function.identity()));
        Map<Long, BillEntity> longBillEntityMap = customerService.getBillMap(billIds);
        if (allCustomer == null) {
            return ResponseEntity.ok("Does not exist customer");
        }
        List<String> customerEmail = allCustomer.stream().map(CustomerEntity::getEmail).collect(Collectors.toList());
        for (CustomerEntity customerEntity: allCustomer) {
            String email = customerEntity.getEmail();
            BillEntity billEntity = longBillEntityMap.get(customerEntity.getId());
            sendMailToCustomer(email, "Thông báo quá hạn  ",
                    "<p>Hóa đơn của khách hàng da qua hạn vào ngày: :</p> <p>" +billEntity.getLimitedTime()+"</p>");
        }
        return ResponseEntity.ok("Email sent successfully");

    }
    @Operation(summary = "sendemailafterpaymenr")
    @PostMapping("/sendEmail-after")
    public ResponseEntity<String> sendEmailAfterPaymenPriceWater(
            @RequestParam List<Long> billIds) {

        List<CustomerEntity> allCustomer = customerService.getAllCustomerByBillIds(billIds);
        Map<Long, CustomerEntity> customerEntityMap = allCustomer.stream().collect(Collectors.toMap(
                CustomerEntity::getId, Function.identity()));
        Map<Long, BillEntity> longBillEntityMap = customerService.getBillMap(billIds);
        if (allCustomer == null) {
            return ResponseEntity.ok("Does not exist customer");
        }
        List<String> customerEmail = allCustomer.stream().map(CustomerEntity::getEmail).collect(Collectors.toList());
        for (CustomerEntity customerEntity : allCustomer) {
            String email = customerEntity.getEmail();
            BillEntity billEntity = longBillEntityMap.get(customerEntity.getId());
            sendMailToCustomer(email, "thông báo đã đóng hóa đơn  ",
                    "<p>Quý khách đã thanh toán thành công số tiền :</p> <p>" + billEntity.getPrice()
                            + "vào ngày: " + billEntity.getSubmitTime() + "</p>");
        }
        return ResponseEntity.ok("Email sent successfully");

    }

    private void sendMailToCustomer(String email, String subject, String content) {
        try {
            emailService.sendEmail(email, subject, content);
        } catch (Exception e) {
            System.out.println("[Email-" + email + "] - Error::" + e.getMessage());
        }
    }

}
