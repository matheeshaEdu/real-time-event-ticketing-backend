package com.iit.oop.eventticketservice.util.validator.integer;

import com.iit.oop.eventticketservice.util.validator.AbstractValidator;
import com.iit.oop.eventticketservice.util.validator.ValidationResult;

public class IntegerValidator extends AbstractValidator<Integer> {
    @Override
    public ValidationResult<Integer> validate(String input) {
        int value;
        try {
            value = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            errorMessage = "Must be an integer";
            return validationFailed(input);
        }
        // accept only positive integers
        if (value < 0) {
            errorMessage = "Integer must be positive";
            return validationFailed(input);
        }
        return ValidationResult.success(value);
    }
}