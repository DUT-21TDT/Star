package com.pbl.star.exceptions;

public class InvalidSignUpParamsException extends ApiErrorException {
    public InvalidSignUpParamsException(String message) {
        super(message);
    }
}
