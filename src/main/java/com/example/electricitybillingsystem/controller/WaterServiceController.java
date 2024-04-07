package com.example.electricitybillingsystem.controller;

import com.example.electricitybillingsystem.model.WaterService;
import com.example.electricitybillingsystem.service.WaterServiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("water-service")
@RequiredArgsConstructor
public class WaterServiceController {
    private final WaterServiceService waterServiceService;

    @PostMapping("/create")
    public Object create(@RequestBody WaterService body) {
        return waterServiceService.create(body);
    }

    @GetMapping("/all")
    public Object getAll() {
        return waterServiceService.getAll();
    }

    @GetMapping("/{id}")
    public Object getOne(@PathVariable Long id) {
        return waterServiceService.getOne(id);
    }

}
