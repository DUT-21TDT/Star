package com.pbl.star.exceptions;

/**
 * This exception is used for throwing when an entity is not found in the database.
 */
public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(String message) {
        super(message);
    }
}
