package com.example.electricitybillingsystem.controller;

import com.example.electricitybillingsystem.phu.BillService2;
import com.example.electricitybillingsystem.service.BillService;
import com.example.electricitybillingsystem.vo.dto.BillBeforePaymentResponse;
import com.example.electricitybillingsystem.vo.dto.BillResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/bill")
@CrossOrigin
public class BillController {
    private final BillService billService;
    private final BillService2 billService2;

    @Operation(summary = "get all bill before payment")
    @GetMapping("/list")
    public Page<BillBeforePaymentResponse> getAllBillBeforePayment(@RequestParam(required = false) Pageable pageable){
        return billService.getAllBillBeforePayment(pageable);
    }

    @Operation(summary = "Theo dõi bill của khách hàng")
    @GetMapping
    public Page<BillResponse> getBills(@RequestParam(required = false) String status,
                                       @RequestParam(required = false) String name,
                                       @RequestParam(required = false) Integer month,
                                       @RequestParam(required = false) Integer year,
                                       @RequestParam(required = false) String order,
                                       @ParameterObject Pageable pageable){
        return billService2.getBills(status, name, month, year, order, pageable);
    }
}
