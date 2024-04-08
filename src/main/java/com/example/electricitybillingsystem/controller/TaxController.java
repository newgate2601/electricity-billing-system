package com.example.electricitybillingsystem.controller;

import com.example.electricitybillingsystem.model.TaxEntity;
import com.example.electricitybillingsystem.service.TaxService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("tax")
@RequiredArgsConstructor
public class TaxController {
    private final TaxService taxService;

    @GetMapping("/all")
    public Object getAll() {
        return taxService.getAll();
    }

    @GetMapping("/{id}")
    public Object getInfo(@PathVariable Long id) {
        return taxService.getOne(id);
    }

    @PostMapping("/update")
    public Object updateTax(@RequestBody TaxEntity taxEntity) {
        return taxService.save(taxEntity);
    }

}
