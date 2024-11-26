package com.pbl.star.exceptions.handlers;

import com.pbl.star.exceptions.InvalidVerificationTokenException;
import com.pbl.star.exceptions.InvalidSignUpParamsException;
import com.pbl.star.exceptions.ModeratorAccessException;
import com.pbl.star.exceptions.ResourceOwnershipException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AuthExceptionHandler {
    @ExceptionHandler({ InvalidSignUpParamsException.class })
    public ResponseEntity<?> handleInvalidSignUpFormException(final Exception e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler({ InvalidVerificationTokenException.class })
    public ResponseEntity<?> handleExpiredVerificationTokenException(final Exception e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler({ AuthorizationDeniedException.class })
    public ResponseEntity<?> handleModeratorAccessException(final Exception e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
    }
}
