package com.uow.eventticketservice.util.validator;

public class ValidationResult<T> {
    private final T value;
    private final String error;

    private ValidationResult(T value, String error) {
        this.value = value;
        this.error = error;
    }

    public static <T> ValidationResult<T> success(T value) {
        return new ValidationResult<>(value, null);
    }

    public static <T> ValidationResult<T> failure(String error) {
        return new ValidationResult<>(null, error);
    }

    public boolean isValid() {
        return value != null;
    }

    public T getValue() {
        return value;
    }

    public String getError() {
        return error;
    }
}