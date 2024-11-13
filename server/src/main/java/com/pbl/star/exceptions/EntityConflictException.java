package com.pbl.star.exceptions;

public class EntityConflictException extends ApiErrorException {
    public EntityConflictException(String message) {
        super(message);
    }
}
