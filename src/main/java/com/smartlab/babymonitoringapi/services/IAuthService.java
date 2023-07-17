package com.smartlab.babymonitoringapi.services;

import com.smartlab.babymonitoringapi.web.dtos.requests.AuthenticationRequest;
import com.smartlab.babymonitoringapi.web.dtos.requests.ValidateTokenRequest;
import com.smartlab.babymonitoringapi.web.dtos.responses.BaseResponse;

public interface IAuthService {
    BaseResponse authenticate(AuthenticationRequest request);
    BaseResponse validateToken(ValidateTokenRequest request);
}
