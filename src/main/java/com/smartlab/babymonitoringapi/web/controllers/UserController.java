package com.smartlab.babymonitoringapi.web.controllers;

import com.smartlab.babymonitoringapi.services.IUserService;
import com.smartlab.babymonitoringapi.web.dtos.requests.UpdateUserMonitorRequest;
import com.smartlab.babymonitoringapi.web.dtos.requests.CreateUserRequest;
import com.smartlab.babymonitoringapi.web.dtos.requests.UpdateUserRequest;
import com.smartlab.babymonitoringapi.web.dtos.responses.BaseResponse;
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

    @PutMapping("monitor")
    public ResponseEntity<BaseResponse> update(@RequestBody @Valid UpdateUserMonitorRequest request) {
      return service.update(request).apply();
    }

    @DeleteMapping("{id}")
    public ResponseEntity<BaseResponse> delete(@PathVariable String id) {
        return service.delete(id).apply();
    }
}
