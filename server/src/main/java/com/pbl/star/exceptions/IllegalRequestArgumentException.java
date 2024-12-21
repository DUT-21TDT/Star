package com.pbl.star.exceptions;

public class IllegalRequestArgumentException extends ApiErrorException {
    public IllegalRequestArgumentException(String message) {
        super(message);
    }
    public IllegalRequestArgumentException(String message, String errorCode) {
        super(message, errorCode);
    }
}
