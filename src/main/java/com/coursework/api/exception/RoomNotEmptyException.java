package com.coursework.api.exception;

public class RoomNotEmptyException extends ConflictException {

    public RoomNotEmptyException(String message) {
        super(message);
    }
}
