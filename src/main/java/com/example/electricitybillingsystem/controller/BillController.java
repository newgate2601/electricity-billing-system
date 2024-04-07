package com.example.electricitybillingsystem.controller;

import com.example.electricitybillingsystem.dto.BillBeforePaymentResponse;
import com.example.electricitybillingsystem.service.BillService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/bill")
public class BillController {
    private final BillService billService;
    @Operation(summary = "get all bill before payment")
    @GetMapping("/list")
    public Page<BillBeforePaymentResponse> getAllBillBeforePayment(@RequestParam(required = false) Pageable pageable){
        return billService.getAllBillBeforePayment(pageable);
    }

}
