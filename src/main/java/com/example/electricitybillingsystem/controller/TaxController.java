package com.example.electricitybillingsystem.controller;

import com.example.electricitybillingsystem.model.TaxEntity;
import com.example.electricitybillingsystem.service.TaxService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("tax")
@RequiredArgsConstructor
public class TaxController {
    private final TaxService taxService;

    @PostMapping("/update")
    public Object updateTax(@RequestBody TaxEntity taxEntity) {
        return taxService.save(taxEntity);
    }

}
