package com.iit.oop.eventticketservice.util.shell;

import com.iit.oop.eventticketservice.service.validator.ValidationResult;
import com.iit.oop.eventticketservice.service.validator.Validator;
import com.iit.oop.eventticketservice.service.validator.integer.IntegerValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;

public class ShellScanner implements ShellClosable {
    private static final Logger log = LoggerFactory.getLogger(ShellScanner.class);
    private static final Scanner scanner = new Scanner(System.in);
    private static final Validator<Integer> integerValidator = new IntegerValidator();

    private ShellScanner() {
        // Prevent instantiation
    }

    private static class ScanHolder {
        private static final ShellScanner INSTANCE = new ShellScanner();
    }

    public static ShellScanner getInstance() {
        return ScanHolder.INSTANCE;
    }

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
            log.error("Exception occurred while parsing input: {} ", input);
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    public String scanString() throws IllegalArgumentException  {
        if (!scanner.hasNextLine()) {
            log.error("No line found in input");
            throw new IllegalArgumentException("No line found in input");
        }

        String input = scanner.nextLine().trim();
        if (input.isEmpty()) {
            log.error("Input cannot be empty");
            throw new IllegalArgumentException("Input cannot be empty");
        }
        if (!input.matches("[a-zA-Z0-9 ]+")) {
            log.error("Input contains invalid characters: {}", input);
            throw new IllegalArgumentException("Input contains invalid characters");
        }
        return input;
    }

    public Scanner getScanner(){
        return scanner;
    }

    public void close() {
        scanner.close();
    }
}
