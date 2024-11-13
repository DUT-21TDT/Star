package com.pbl.star.exceptions;

public class RequiredFieldMissingException extends ApiErrorException {
    public RequiredFieldMissingException(String message) {
        super(message);
    }
}
