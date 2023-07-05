package com.smartlab.babymonitoringapi.services;

import com.smartlab.babymonitoringapi.controllers.dtos.requests.AuthenticationRequest;
import com.smartlab.babymonitoringapi.controllers.dtos.responses.BaseResponse;

public interface IAuthService {

    BaseResponse authenticate(AuthenticationRequest request);
}
