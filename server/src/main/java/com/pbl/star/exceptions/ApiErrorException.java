package com.pbl.star.exceptions;

import lombok.Getter;

@Getter
public abstract class ApiErrorException extends RuntimeException {

    private String errorCode;

    public ApiErrorException(String message) {
        super(message);
    }

    public ApiErrorException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}
