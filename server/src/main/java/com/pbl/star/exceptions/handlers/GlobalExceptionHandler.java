package com.pbl.star.exceptions.handlers;

import com.pbl.star.exceptions.EntityConflictException;
import com.pbl.star.exceptions.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler({ EntityConflictException.class })
    public ResponseEntity<?> handleUserAlreadyExistException(final Exception e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }

    @ExceptionHandler({ EntityNotFoundException.class })
    public ResponseEntity<?> handleEntityNotFoundException(final Exception e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler({ RuntimeException.class })
    public ResponseEntity<?> handleException(final Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }
}
