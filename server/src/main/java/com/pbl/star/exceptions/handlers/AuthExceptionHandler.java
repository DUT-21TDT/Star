package com.pbl.star.exceptions.handlers;

import com.pbl.star.exceptions.InvalidVerificationTokenException;
import com.pbl.star.exceptions.InvalidSignUpFormException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AuthExceptionHandler {
    @ExceptionHandler({ InvalidSignUpFormException.class })
    public ResponseEntity<?> handleInvalidSignUpFormException(final Exception e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler({ InvalidVerificationTokenException.class })
    public ResponseEntity<?> handleExpiredVerificationTokenException(final Exception e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
}
