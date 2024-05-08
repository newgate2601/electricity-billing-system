package com.example.electricitybillingsystem.controller;

import com.example.electricitybillingsystem.phu.BillService2;
import com.example.electricitybillingsystem.service.BillService;
import com.example.electricitybillingsystem.service.TieredPricingService;
import com.example.electricitybillingsystem.vo.dto.BillAfterPaymentResponse;
import com.example.electricitybillingsystem.vo.dto.BillBeforePaymentResponse;
import com.example.electricitybillingsystem.vo.dto.DetailBillResponse;
import com.example.electricitybillingsystem.vo.response.AdjustPricingResponse;
import com.example.electricitybillingsystem.vo.dto.BillResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/bill")
@CrossOrigin
public class BillController {
    private final BillService billService;
    private final BillService2 billService2;

    private final TieredPricingService tieredPricingService;

    @Operation(summary = "get all bill before payment")
    @GetMapping("/list")
    public Page<BillBeforePaymentResponse> getAllBillBeforePayment(@ParameterObject Pageable pageable) {
        return billService.getAllBillBeforePayment(pageable);
    }
    @Operation(summary = "get all bill over time")
    @GetMapping("/list/over-time")
    public Page<BillAfterPaymentResponse> getAllBillOverTime(@ParameterObject Pageable pageable){
        return billService.getAllBillOverTime(pageable);
    }

    @Operation(summary = "get all bill after payment")
    @GetMapping("/list/after")
    public Page<BillAfterPaymentResponse> getAllBillAfterPayment(@ParameterObject Pageable pageable){
        return billService.getAllBillAfterPayment(pageable);
    }

    @GetMapping("/test1")
    public void test1() throws InterruptedException {
        Thread.sleep(1000);
    }

    @GetMapping("/test2")
    public void test2() throws InterruptedException {
        Thread.sleep(4000);
    }

    @Operation(summary = "Theo dõi bill của khách hàng")
    @GetMapping
    public Page<BillResponse> getBills(@RequestParam(required = false) String statusValue,
                                       @RequestParam(required = false) String name,
                                       @RequestParam(required = false) Integer month,
                                       @RequestParam(required = false) Integer year,
                                       @RequestParam(required = false) String order,
                                       @ParameterObject Pageable pageable){
        return billService2.getBills(statusValue, name, month, year, order, pageable);
    }
    @Operation(summary = "notification adjust service pricing")
    @GetMapping("/adjust-pricing")
    public AdjustPricingResponse getAllAdjustPricing() {
        return tieredPricingService.getAllByStatus();
    }

    @Operation(summary = "get detail bill")
    @GetMapping("/detail/bill")
    public List<DetailBillResponse> bigDecimal(@RequestParam Long billId) {
        return billService.intoMoney(billId);
    }
}
