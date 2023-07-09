package com.smartlab.babymonitoringapi.controllers;

import com.smartlab.babymonitoringapi.controllers.dtos.requests.CreateUserRequest;
import com.smartlab.babymonitoringapi.controllers.dtos.requests.UpdateUserRequest;
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

    @PostMapping
    public ResponseEntity<BaseResponse> create(@RequestBody @Valid CreateUserRequest request) {
        return service.create(request).apply();
    }

    @GetMapping("{id}")
    public ResponseEntity<BaseResponse> get(@PathVariable String id) {
        return service.get(id).apply();
    }

    @PutMapping("{id}")
    public ResponseEntity<BaseResponse> update(@PathVariable String id, @RequestBody @Valid UpdateUserRequest request) {
        return service.update(request, id).apply();
    }

    @DeleteMapping("{id}")
    public ResponseEntity<BaseResponse> delete(@PathVariable String id) {
        return service.delete(id).apply();
    }
}
