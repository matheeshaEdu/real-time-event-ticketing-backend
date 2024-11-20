package com.iit.oop.eventticketservice.service.validator;

public interface Validator<T> {
    ValidationResult<T> validate(String input);
}
