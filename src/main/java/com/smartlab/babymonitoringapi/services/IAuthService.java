package com.smartlab.babymonitoringapi.services;

import com.smartlab.babymonitoringapi.web.dtos.requests.AuthenticationRequest;
import com.smartlab.babymonitoringapi.web.dtos.requests.VerifyTokenRequest;
import com.smartlab.babymonitoringapi.web.dtos.responses.BaseResponse;

public interface IAuthService {
    BaseResponse authenticate(AuthenticationRequest request);
    BaseResponse validateToken(VerifyTokenRequest request);
}
