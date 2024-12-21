package com.pbl.star.exceptions;

public class InvalidSignUpParamsException extends ApiErrorException {
    public InvalidSignUpParamsException(String message) {
        super(message);
    }
    public InvalidSignUpParamsException(String message, String errorCode) {
        super(message, errorCode);
    }
}
