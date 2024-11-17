package com.iit.oop.eventticketservice.service.cli.input;

import com.iit.oop.eventticketservice.service.validator.ValidationResult;
import com.iit.oop.eventticketservice.service.validator.Validator;
import com.iit.oop.eventticketservice.service.validator.integer.IntegerValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class Scan {
    private static final Logger log = LoggerFactory.getLogger(Scan.class);
    private static final Scanner scanner = new Scanner(System.in);
    private static final Validator<Integer> integerValidator = new IntegerValidator();

    public int scanPositiveInt() throws IllegalArgumentException {
        String input = scanner.nextLine().trim();
        try {
            ValidationResult<Integer> result = integerValidator.validate(input);
            if (!result.isValid()) {
                log.error("Invalid input: {}", input);
                throw new IllegalArgumentException(result.getError());
            } else {
                return result.getValue();
            }
        } catch (Exception e) {
            log.error("Exception occurred while parsing input: {}", input);
            throw new IllegalArgumentException(e.getMessage());
        }
    }
}
