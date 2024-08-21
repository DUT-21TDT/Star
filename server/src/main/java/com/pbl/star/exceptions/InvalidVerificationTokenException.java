package com.pbl.star.exceptions;

public class InvalidVerificationTokenException extends RuntimeException {
    public InvalidVerificationTokenException(String message) {
        super(message);
    }
}
