package com.example.electricitybillingsystem.controller;

import com.example.electricitybillingsystem.service.BillService;
import com.example.electricitybillingsystem.service.TieredPricingService;
import com.example.electricitybillingsystem.vo.dto.BillAfterPaymentResponse;
import com.example.electricitybillingsystem.vo.dto.BillBeforePaymentResponse;
import com.example.electricitybillingsystem.vo.dto.DetailBillResponse;
import com.example.electricitybillingsystem.vo.response.AdjustPricingResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/bill")
@CrossOrigin
public class BillController {
    private final BillService billService;
    private final TieredPricingService tieredPricingService;


    @Operation(summary = "get all bill before payment")
    @GetMapping("/list")
    public Page<BillBeforePaymentResponse> getAllBillBeforePayment(@RequestParam(required = false) Pageable pageable) {
        return billService.getAllBillBeforePayment(pageable);
    }

    @Operation(summary = "get all bill after payment")
    @GetMapping("/list/after")
    public Page<BillAfterPaymentResponse> getAllBillAfterPayment(@RequestParam(required = false) Pageable pageable){
        return billService.getAllBillAfterPayment(pageable);
    }

    @Operation(summary = "notification adjust service pricing")
    @GetMapping("/adjust-pricing")
    public AdjustPricingResponse getAllAdjustPricing() {
        return tieredPricingService.getAllByStatus();
    }

    @Operation(summary = "get detail bill")
    @GetMapping
    public List<DetailBillResponse> bigDecimal() {
        return billService.intoMoney(1L, 1L, 20L);
    }
}
