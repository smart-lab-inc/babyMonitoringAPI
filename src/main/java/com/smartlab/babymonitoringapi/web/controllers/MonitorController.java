package com.smartlab.babymonitoringapi.web.controllers;

import com.smartlab.babymonitoringapi.services.IMonitorService;
import com.smartlab.babymonitoringapi.web.dtos.responses.BaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("monitor")
public class MonitorController {

    @Autowired
    private IMonitorService service;

    @PostMapping
    public ResponseEntity<BaseResponse> create() {
        return service.create().apply();
    }
}
