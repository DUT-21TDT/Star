package com.pbl.star.exceptions.handlers;

import com.pbl.star.exceptions.InvalidSignUpFormException;
import com.pbl.star.exceptions.UserAlreadyExistException;
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

    @ExceptionHandler({ UserAlreadyExistException.class })
    public ResponseEntity<?> handleUserAlreadyExistException(final Exception e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }
}
