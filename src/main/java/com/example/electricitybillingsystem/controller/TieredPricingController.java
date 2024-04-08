package com.example.electricitybillingsystem.controller;

import com.example.electricitybillingsystem.service.TieredPricingService;
import com.example.electricitybillingsystem.vo.request.UpdatePriceRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("tiered-pricing")
@RequiredArgsConstructor
public class TieredPricingController {
    private final TieredPricingService tieredPricingService;

    @PostMapping("/update")
    public Object updatePrice(@RequestBody UpdatePriceRequest request) {
        return tieredPricingService.updatePrice(request);
    }

}
