package com.smartlab.babymonitoringapi.services;

import com.smartlab.babymonitoringapi.controllers.dtos.requests.CreateUserRequest;
import com.smartlab.babymonitoringapi.controllers.dtos.responses.BaseResponse;

public interface IUserService {

    BaseResponse create(CreateUserRequest request);
}
