package com.pbl.star.exceptions.handlers;

import com.pbl.star.dtos.response.ErrorResponse;
import com.pbl.star.exceptions.*;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler({ EntityConflictException.class })
    public ResponseEntity<?> handleUserAlreadyExistException(final ApiErrorException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(
                new ErrorResponse(e.getErrorCode(), e.getMessage())
        );
    }

    @ExceptionHandler({ RequiredFieldMissingException.class })
    public ResponseEntity<?> handleRequiredFieldMissingException(final ApiErrorException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ErrorResponse(e.getErrorCode(), e.getMessage())
        );
    }

    @ExceptionHandler({ EntityNotFoundException.class })
    public ResponseEntity<?> handleEntityNotFoundException(final ApiErrorException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ErrorResponse(e.getErrorCode(), e.getMessage())
        );
    }

    @ExceptionHandler({ IllegalRequestArgumentException.class })
    public ResponseEntity<?> handleIllegalRequestArgumentException(final ApiErrorException e) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(
                new ErrorResponse(e.getErrorCode(), e.getMessage())
        );
    }

    @ExceptionHandler({ ModeratorAccessException.class })
    public ResponseEntity<?> handleModeratorAccessException(final ApiErrorException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                new ErrorResponse(e.getErrorCode(), e.getMessage())
        );
    }

    @ExceptionHandler({ ResourceOwnershipException.class })
    public ResponseEntity<?> handleResourceOwnershipException(final ApiErrorException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                new ErrorResponse(e.getErrorCode(), e.getMessage())
        );
    }

    @ExceptionHandler({ ConversionFailedException.class })
    public ResponseEntity<?> handleConversionFailedException(final Exception e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ErrorResponse("BAD_REQUEST", e.getMessage())
        );
    }

    @ExceptionHandler({ BindException.class })
    public ResponseEntity<?> handleValidationExceptions(final BindException ex) {
        String errorMessage = ex.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                .reduce("", (acc, message) -> acc + message + "\n");

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ErrorResponse("BAD_REQUEST", errorMessage)
        );
    }

    @ExceptionHandler({ RuntimeException.class })
    public ResponseEntity<?> handleException(final Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                new ErrorResponse("INTERNAL_SERVER_ERROR", e.getMessage())
        );
    }
}
