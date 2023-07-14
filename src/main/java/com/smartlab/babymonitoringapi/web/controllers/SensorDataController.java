package com.smartlab.babymonitoringapi.web.controllers;

import com.smartlab.babymonitoringapi.services.ISensorDataService;
import com.smartlab.babymonitoringapi.web.dtos.responses.BaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("sensor-data")
public class SensorDataController {
    @Autowired
    private ISensorDataService service;

    @GetMapping("monitor/{monitorId}")
    public ResponseEntity<BaseResponse> get(@PathVariable String monitorId,
                                            @RequestParam(defaultValue = "") String sensorName,
                                            @RequestParam (defaultValue = "") String startTime,
                                            @RequestParam (defaultValue = "") String endTime) {
        return service.get(monitorId, sensorName, startTime, endTime).apply();
    }
}

