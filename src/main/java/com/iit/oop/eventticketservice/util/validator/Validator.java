package com.iit.oop.eventticketservice.util.validator;

public interface Validator<T> {
    ValidationResult<T> validate(String input);
}
