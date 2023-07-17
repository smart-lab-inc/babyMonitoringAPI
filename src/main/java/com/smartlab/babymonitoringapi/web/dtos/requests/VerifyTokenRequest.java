package com.smartlab.babymonitoringapi.web.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class VerifyTokenRequest {
    @NotBlank
    private String token;
}
