package com.uow.eventticketservice.util.validator;

import com.uow.eventticketservice.util.validator.integer.IntegerValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility class to validate input
 */
public class ValidationUtil {
    private static final Logger log = LoggerFactory.getLogger(ValidationUtil.class);
    private static final Validator<Integer> integerValidator = new IntegerValidator();

    private ValidationUtil() {
        // Prevent instantiation
    }

    /**
     * Validate input as positive integer
     *
     * @param input input to validate
     * @return validated positive integer
     * @throws IllegalArgumentException if input is invalid
     */
    public static int validatePositiveInt(String input) throws IllegalArgumentException {
        try {
            ValidationResult<Integer> result = integerValidator.validate(input);
            if (!result.isValid()) {
                log.error("Invalid input: {}", input);
                throw new IllegalArgumentException(result.getError());
            } else {
                return result.getValue();
            }
        } catch (Exception e) {
            log.error("Exception occurred while parsing input: {} ", input);
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    public static int validatePositiveInt(int input) throws IllegalArgumentException {
        if (input <= 0) {
            log.error("Invalid input !! Input should be positive");
            throw new IllegalArgumentException("Input must be a positive integer");
        } else {
            return input;
        }
    }
}
