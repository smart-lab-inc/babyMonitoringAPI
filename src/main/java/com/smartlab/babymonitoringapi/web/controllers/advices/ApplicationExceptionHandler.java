package com.smartlab.babymonitoringapi.web.controllers.advices;

import com.smartlab.babymonitoringapi.web.controllers.exceptions.AccessDeniedException;
import com.smartlab.babymonitoringapi.web.controllers.exceptions.IllegalArgumentException;
import com.smartlab.babymonitoringapi.web.controllers.exceptions.ObjectNotFoundException;
import com.smartlab.babymonitoringapi.web.controllers.exceptions.UniqueConstraintViolationException;
import com.smartlab.babymonitoringapi.web.dtos.responses.BaseResponse;
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
                .statusCode(HttpStatus.BAD_REQUEST.value())
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
                .statusCode(HttpStatus.NOT_FOUND.value())
                .build().apply();
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<BaseResponse> handleAccessDeniedException(AccessDeniedException ex) {
        Map<String, String> errors = new HashMap<>();

        errors.put("message", "Access to the requested resource is forbidden");

        return BaseResponse.builder()
                .data(errors)
                .message("Operation failed")
                .success(Boolean.FALSE)
                .status(HttpStatus.FORBIDDEN)
                .build().apply();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<BaseResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
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
}
