package com.pbl.star.exceptions.handlers;

import com.pbl.star.dtos.response.ErrorResponse;
import com.pbl.star.exceptions.ApiErrorException;
import com.pbl.star.exceptions.InvalidVerificationTokenException;
import com.pbl.star.exceptions.InvalidSignUpParamsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AuthExceptionHandler {
    @ExceptionHandler({ InvalidSignUpParamsException.class })
    public ResponseEntity<?> handleInvalidSignUpFormException(final ApiErrorException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ErrorResponse(e.getErrorCode(), e.getMessage())
        );
    }

    @ExceptionHandler({ InvalidVerificationTokenException.class })
    public ResponseEntity<?> handleExpiredVerificationTokenException(final ApiErrorException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ErrorResponse(e.getErrorCode(), e.getMessage())
        );
    }

    @ExceptionHandler({ AccessDeniedException.class })
    public ResponseEntity<?> handleAccessDeniedException(final Exception e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                new ErrorResponse("FORBIDDEN", e.getMessage())
        );
    }
}
