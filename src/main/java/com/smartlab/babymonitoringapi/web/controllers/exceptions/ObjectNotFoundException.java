package com.smartlab.babymonitoringapi.web.controllers.exceptions;

public class ObjectNotFoundException extends RuntimeException {

    public ObjectNotFoundException(String message) {
        super(message);
    }
}
