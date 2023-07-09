package com.smartlab.babymonitoringapi.controllers.exceptions;

import org.springframework.dao.DataIntegrityViolationException;

public class UniqueConstraintViolationException extends DataIntegrityViolationException {

    public UniqueConstraintViolationException(String msg) {
        super(msg);
    }
}
