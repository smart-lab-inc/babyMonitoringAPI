package com.smartlab.babymonitoringapi.controllers;

import com.smartlab.babymonitoringapi.controllers.dtos.responses.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("health")
public class HealthController {

    @GetMapping
    public BaseResponse healthCheck() {
        return BaseResponse.builder()
                .message("Server running!")
                .status(HttpStatus.OK)
                .statusCode(HttpStatus.OK.value())
                .success(true)
                .build();
    }
}
