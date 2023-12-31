package com.smartlab.babymonitoringapi.web.dtos.responses;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UserResponse {
    private String id;

    private String email;

    private String firstName;

    private String lastName;

    private String phoneNumber;
}
