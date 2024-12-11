package com.uow.eventticketservice.util.shell;

import com.uow.eventticketservice.util.validator.ValidationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;

public class ShellScanner implements ShellClosable {
    private static final Logger log = LoggerFactory.getLogger(ShellScanner.class);
    private static final Scanner scanner = new Scanner(System.in);

    private ShellScanner() {
        // Prevent instantiation
    }

    public static ShellScanner getInstance() {
        return ScanHolder.INSTANCE;
    }

    public int scanPositiveInt() throws IllegalArgumentException {
        String input = scanner.nextLine().trim();
        return ValidationUtil.validatePositiveInt(input);
    }

    public synchronized String scanString() throws IllegalArgumentException {
        try {
            if (!scanner.hasNextLine()) {
                log.error("No line found in input");
                throw new IllegalArgumentException("No line found in input. Please provide valid input.");
            }

            String input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                log.error("Input cannot be empty");
                throw new IllegalArgumentException("Input cannot be empty.");
            }

            if (!input.matches("[a-zA-Z0-9 ]+")) {
                log.error("Input contains invalid characters: {}", input);
                throw new IllegalArgumentException("Input contains invalid characters. Only alphanumeric and spaces are allowed.");
            }

            return input;
        } catch (IllegalStateException e) {
            String errorMessage = "Scanner is unavailable for reading input. Error: " + e.getMessage();
            log.error(errorMessage, e);
            throw new IllegalStateException(errorMessage, e);
        }
    }

    public void close() {
        scanner.close();
    }

    private static class ScanHolder {
        private static final ShellScanner INSTANCE = new ShellScanner();
    }
}
