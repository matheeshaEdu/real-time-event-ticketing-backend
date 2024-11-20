package com.iit.oop.eventticketservice.service.validator;

public abstract class AbstractValidator<T> implements Validator<T> {
    protected String errorMessage;

    protected ValidationResult<T> validationFailed(String input) {
        return ValidationResult.failure(errorMessage + " Input: " + input);
    }
}
