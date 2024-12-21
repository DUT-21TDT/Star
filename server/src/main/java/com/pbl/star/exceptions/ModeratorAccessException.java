package com.pbl.star.exceptions;

public class ModeratorAccessException extends ApiErrorException {
    public ModeratorAccessException(String message) {
        super(message);
    }
    public ModeratorAccessException(String message, String errorCode) {
        super(message, errorCode);
    }
}
