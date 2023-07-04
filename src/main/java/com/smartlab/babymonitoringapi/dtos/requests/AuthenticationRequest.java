package com.smartlab.babymonitoringapi.dtos.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

@Getter
public class AuthenticationRequest {

    @Email
    @NotEmpty
    private String email;

    @NotEmpty
    private String password;
}
