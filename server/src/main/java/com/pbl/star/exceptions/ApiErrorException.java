package com.pbl.star.exceptions;

public abstract class ApiErrorException extends RuntimeException {
    public ApiErrorException(String message) {
        super(message);
    }
}
