package com.smartlab.babymonitoringapi.web.dtos.responses;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Builder
@Getter
@Setter
public class BaseResponse {

    private HttpStatus status;

    private Integer statusCode;

    private String message;

    private Boolean success;

    private Object data;

    public ResponseEntity<BaseResponse> apply() {
        return new ResponseEntity<>(this, status);
    }
}
