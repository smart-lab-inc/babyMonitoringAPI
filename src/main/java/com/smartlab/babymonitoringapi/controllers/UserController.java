package com.smartlab.babymonitoringapi.controllers;

import com.smartlab.babymonitoringapi.controllers.dtos.requests.CreateUserRequest;
import com.smartlab.babymonitoringapi.controllers.dtos.responses.BaseResponse;
import com.smartlab.babymonitoringapi.services.IUserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private IUserService service;

//  TODO: Implementar los métodos que faltan (get, put, delete)

    @PostMapping("create")
    public ResponseEntity<BaseResponse> create(@RequestBody @Valid CreateUserRequest request) {
        return service.create(request).apply();
    }
}
