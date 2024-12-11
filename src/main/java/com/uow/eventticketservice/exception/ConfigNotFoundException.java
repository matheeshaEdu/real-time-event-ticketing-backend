package com.uow.eventticketservice.exception;

/**
 * Exception thrown when a configuration is not found.
 */
public class ConfigNotFoundException extends RuntimeException {
    public ConfigNotFoundException(String message) {
        super(message);
    }
}
