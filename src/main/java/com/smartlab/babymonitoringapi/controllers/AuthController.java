package com.smartlab.babymonitoringapi.controllers;

import com.smartlab.babymonitoringapi.dtos.requests.AuthenticationRequest;
import com.smartlab.babymonitoringapi.dtos.responses.BaseResponse;
import com.smartlab.babymonitoringapi.services.IAuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private IAuthService service;

    @PostMapping("/token")
    public ResponseEntity<BaseResponse> authentication(@RequestBody @Valid AuthenticationRequest request) {
        BaseResponse response = service.authenticate(request);

        return new ResponseEntity<>(response, response.getStatus());
    }
}
