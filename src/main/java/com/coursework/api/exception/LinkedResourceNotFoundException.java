package com.coursework.api.exception;

public class LinkedResourceNotFoundException extends UnprocessableEntityException {

    public LinkedResourceNotFoundException(String message) {
        super(message);
    }
}
