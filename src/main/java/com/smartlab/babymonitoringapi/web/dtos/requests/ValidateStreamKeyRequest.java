package com.smartlab.babymonitoringapi.web.dtos.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ValidateStreamKeyRequest {
    private String key;
}
