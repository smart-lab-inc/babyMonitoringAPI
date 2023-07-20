package com.smartlab.babymonitoringapi.web.controllers;

import com.smartlab.babymonitoringapi.services.IMonitorService;
import com.smartlab.babymonitoringapi.web.dtos.requests.CreateMonitorRequest;
import com.smartlab.babymonitoringapi.web.dtos.responses.BaseResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("monitor")
public class MonitorController {

    @Autowired
    private IMonitorService service;

    @PostMapping
    public ResponseEntity<BaseResponse> create(@RequestBody @Valid CreateMonitorRequest request) {
        return service.create(request).apply();
    }

    @GetMapping("user/{userId}")
    public ResponseEntity<BaseResponse> listByUserId(@PathVariable String userId) {
        return service.listByUserId(userId).apply();
    }
}
