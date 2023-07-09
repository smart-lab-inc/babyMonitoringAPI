package com.smartlab.babymonitoringapi.controllers.advices;

import com.smartlab.babymonitoringapi.controllers.dtos.responses.BaseResponse;
import com.smartlab.babymonitoringapi.controllers.exceptions.AccessDeniedException;
import com.smartlab.babymonitoringapi.controllers.exceptions.ObjectNotFoundException;
import com.smartlab.babymonitoringapi.controllers.exceptions.UniqueConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ApplicationExceptionHandler {

    @ExceptionHandler(UniqueConstraintViolationException.class)
    public ResponseEntity<BaseResponse> handleuUniqueConstraintViolationExceptionResponseEntity(UniqueConstraintViolationException ex) {
        Map<String, String> errors = new HashMap<>();

        String message = ex.getMessage();

        errors.put("message", message);

        return BaseResponse.builder()
                .data(errors)
                .message("Operation failed")
                .success(Boolean.FALSE)
                .status(HttpStatus.BAD_REQUEST)
                .build().apply();
    }

    @ExceptionHandler(ObjectNotFoundException.class)
    public ResponseEntity<BaseResponse> handleObjectNotFoundException(ObjectNotFoundException ex) {
        Map<String, String> errors = new HashMap<>();

        String message = ex.getMessage();

        errors.put("message", message);

        return BaseResponse.builder()
                .data(errors)
                .message("Operation failed")
                .success(Boolean.FALSE)
                .status(HttpStatus.NOT_FOUND)
                .build().apply();
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<BaseResponse> handleAccessDeniedException(AccessDeniedException ex) {
        Map<String, String> errors = new HashMap<>();

        System.out.println(ex.getMessage());

        errors.put("message", "Access to the requested resource is forbidden");

        return BaseResponse.builder()
                .data(errors)
                .message("Operation failed")
                .success(Boolean.FALSE)
                .status(HttpStatus.FORBIDDEN)
                .build().apply();
    }
}
