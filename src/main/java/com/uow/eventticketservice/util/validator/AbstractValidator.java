package com.uow.eventticketservice.util.validator;

public abstract class AbstractValidator<T> implements Validator<T> {
    protected String errorMessage;

    protected ValidationResult<T> validationFailed(String input) {
        return ValidationResult.failure(errorMessage + " Input: " + input);
    }
}
