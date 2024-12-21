package com.pbl.star.exceptions;

public class InvalidVerificationTokenException extends ApiErrorException {
    public InvalidVerificationTokenException(String message) {
        super(message);
    }
    public InvalidVerificationTokenException(String message, String errorCode) {
        super(message, errorCode);
    }
}
