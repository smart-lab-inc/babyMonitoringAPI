package com.smartlab.babymonitoringapi.services;

import com.smartlab.babymonitoringapi.dtos.requests.AuthenticationRequest;
import com.smartlab.babymonitoringapi.dtos.responses.BaseResponse;

public interface IAuthService {

    BaseResponse authenticate(AuthenticationRequest request);
}
