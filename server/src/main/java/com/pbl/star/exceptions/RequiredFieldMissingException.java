package com.pbl.star.exceptions;

public class RequiredFieldMissingException extends ApiErrorException {
    public RequiredFieldMissingException(String message) {
        super(message);
    }
    public RequiredFieldMissingException(String message, String errorCode) {
        super(message, errorCode);
    }
}
