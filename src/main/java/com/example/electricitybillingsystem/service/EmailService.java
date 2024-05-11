package com.example.electricitybillingsystem.service;

import com.example.electricitybillingsystem.model.BillEntity;
import com.example.electricitybillingsystem.model.CustomerEntity;
import com.example.electricitybillingsystem.vo.request.TurnOffWaterInfoRequest;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    private final AddressService addressService;
    private final CustomerService customerService;
    private final TaxService taxService;
    private final TieredPricingService tieredPricingService;

    public void sendEmail(String email, String subject, String content) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom("vua6040@gmail.com", "N04");
        helper.setTo(email);

        helper.setSubject(subject);
        helper.setText(content, true);

        mailSender.send(message);
    }
    @Transactional
    public String sendEmailTurnOffWater(TurnOffWaterInfoRequest turnOffWaterInfoRequest) throws MessagingException, UnsupportedEncodingException {
        String startTime = turnOffWaterInfoRequest.getStartTime();
        String endTime = turnOffWaterInfoRequest.getEndTime();
        if (endTime.equals("") || startTime.equals("")) {
            return "Bạn phải chọn thời gian bắt đầu và kết thúc!";
        }

        List<CustomerEntity> customers = addressService.getAllCustomerByWard(turnOffWaterInfoRequest.getWard());
        if (customers == null || customers.size() == 0) {
            return "Does not exist customer";
        }

        List<String> customerEmail = customers.stream().map(CustomerEntity::getEmail).collect(Collectors.toList());
        for (String email : customerEmail) {
            sendEmail(email, "Thông báo cắt nước", "<p>Thời gian tới sẽ cắt nước trong khoảng thời gian từ:</p> <p>" + startTime + " tới " + endTime + "</p>");
        }
        return "Email sent successfully";
    }
    @Transactional
    public String sendEmailAdjustWater(Map<String, String> request) throws MessagingException, UnsupportedEncodingException {
        List<CustomerEntity> allCustomer = customerService.getAllCustomer();
        if (allCustomer == null) {
            return "Does not exist customer";
        }
        List<String> customerEmail = allCustomer.stream().map(CustomerEntity::getEmail).collect(Collectors.toList());
        for (String email : customerEmail) {
            sendEmail(email, "Thông báo điều chỉnh giá: ", request.get("content"));
        }
        taxService.updateAllStatusToFalse();
        tieredPricingService.updateAllStatusToFalse();
        return "Email sent successfully";
    }
    @Transactional
    public String sendEmailBeforPayment(List<Long> billIds) throws MessagingException, UnsupportedEncodingException {
        List<CustomerEntity> allCustomer = customerService.getAllCustomerByBillIds(billIds);
        Map<Long, BillEntity> longBillEntityMap = customerService.getBillMap(billIds);
        if (allCustomer == null) {
            return "Does not exist customer";
        }
        for (CustomerEntity customerEntity : allCustomer) {
            String email = customerEntity.getEmail();
            BillEntity billEntity = longBillEntityMap.get(customerEntity.getId());
            sendEmail(email, "Thông báo hóa đơn cần đóng ",
                    "<p>Hóa đơn của khách hàng cần thanh toán: :</p> <p>" + billEntity.getPrice()
                            + "số nước cũ: " + billEntity.getStartNumber() +
                            "số nước mới: " + billEntity.getEndNumber() + "</p>");
        }
        return "Email sent successfully";
    }
    @Transactional
    public String sendEmailAfterPayment(List<Long> billIds) throws MessagingException, UnsupportedEncodingException {
        List<CustomerEntity> allCustomer = customerService.getAllCustomerByBillIds(billIds);

        Map<Long, BillEntity> longBillEntityMap = customerService.getBillMap(billIds);
        if (allCustomer == null) {
            return "Does not exist customer";
        }
        for (CustomerEntity customerEntity : allCustomer) {
            String email = customerEntity.getEmail();
            BillEntity billEntity = longBillEntityMap.get(customerEntity.getId());
            sendEmail(email, "Thông báo thanh toán thành công ",
                    "<p>Hóa đơn của khách hàng với giá : :</p> <p>" + billEntity.getPrice()
                            + "số nước cũ: " + billEntity.getStartNumber() +
                            "số nước mới: " + billEntity.getEndNumber() + "</p>"+
                            "<p>"+ "Đã được thanh toán vào ngày" +billEntity.getSubmitTime()+"</p>"
            );
        }
        return "Email sent successfully";
    }
@Transactional
    public String sendEmailOverTime(List<Long> billIds) throws MessagingException, UnsupportedEncodingException {
        List<CustomerEntity> allCustomer = customerService.getAllCustomerByBillIds(billIds);
        Map<Long, CustomerEntity> customerEntityMap = allCustomer.stream().collect(Collectors.toMap(
                CustomerEntity::getId, Function.identity()));
        Map<Long, BillEntity> longBillEntityMap = customerService.getBillMap(billIds);
        if (allCustomer == null) {
            return "Does not exist customer";
        }
        List<String> customerEmail = allCustomer.stream().map(CustomerEntity::getEmail).collect(Collectors.toList());
        for (CustomerEntity customerEntity: allCustomer) {
            String email = customerEntity.getEmail();
            BillEntity billEntity = longBillEntityMap.get(customerEntity.getId());
            sendEmail(email, "Thông báo quá hạn  ",
                    "<p>Hóa đơn của khách hàng da qua hạn vào ngày: :</p> <p>" +billEntity.getLimitedTime()+"</p>");
        }
        return "Email sent successfully";
    }

//    public List<CustomerEntity> sendEmailTurnOffWater(String ward) {
//        return addressService.getAllCustomerByWard(ward);
//    }

}
