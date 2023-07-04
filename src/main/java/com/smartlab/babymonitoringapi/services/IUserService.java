package com.smartlab.babymonitoringapi.services;

import com.smartlab.babymonitoringapi.dtos.requests.CreateUserRequest;
import com.smartlab.babymonitoringapi.dtos.responses.BaseResponse;

public interface IUserService {

    BaseResponse create(CreateUserRequest request);
}
