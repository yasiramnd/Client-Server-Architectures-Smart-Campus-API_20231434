package com.coursework.api.exception;

public class SensorUnavailableException extends ForbiddenOperationException {

    public SensorUnavailableException(String message) {
        super(message);
    }
}
