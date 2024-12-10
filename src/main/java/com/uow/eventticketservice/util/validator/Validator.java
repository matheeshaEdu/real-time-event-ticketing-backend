package com.uow.eventticketservice.util.validator;

public interface Validator<T> {
    ValidationResult<T> validate(String input);
}
