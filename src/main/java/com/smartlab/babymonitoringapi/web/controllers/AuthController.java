package com.smartlab.babymonitoringapi.web.controllers;

import com.smartlab.babymonitoringapi.services.IAuthService;
import com.smartlab.babymonitoringapi.web.dtos.requests.AuthenticationRequest;
import com.smartlab.babymonitoringapi.web.dtos.requests.ValidateTokenRequest;
import com.smartlab.babymonitoringapi.web.dtos.responses.BaseResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("auth")
public class AuthController {

    @Autowired
    private IAuthService service;

    @PostMapping("token")
    public ResponseEntity<BaseResponse> authentication(@RequestBody @Valid AuthenticationRequest request) {
        return service.authenticate(request).apply();
    }

    // support "Content-Type 'application/x-www-form-urlencoded;charset=UTF-8'
    @PostMapping(value = "validate-token", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<BaseResponse> verifyToken(@RequestBody ValidateTokenRequest request) {
        return service.validateToken(request).apply();
    }
}
