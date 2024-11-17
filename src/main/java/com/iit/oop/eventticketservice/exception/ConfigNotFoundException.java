package com.iit.oop.eventticketservice.exception;

public class ConfigNotFoundException extends RuntimeException {
    public ConfigNotFoundException(String message) {
        super(message);
    }
}
