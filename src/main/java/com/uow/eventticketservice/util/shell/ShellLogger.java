package com.uow.eventticketservice.util.shell;

import java.io.PrintWriter;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * A thread-safe shell logger with configurable log levels and output mechanisms.
 */
public class ShellLogger implements ShellClosable {

    private final PrintWriter writer = new PrintWriter(System.out);
    private final Lock lock = new ReentrantLock(true); // Fair lock

    // Private constructor for Singleton
    private ShellLogger() {
    }

    /**
     * Returns the singleton instance of ShellLogger.
     *
     * @return The singleton instance.
     */
    public static ShellLogger getInstance() {
        return ShellLoggerHolder.instance;
    }

    public void info(String message) {
        log(message, LogLevel.INFO);
    }

    public void usageInfo(String message) {
        logWithoutLabel(message, LogLevel.USAGE);
    }

    public void warn(String message) {
        log(message, LogLevel.WARN);
    }

    public void error(String message) {
        log(message, LogLevel.ERROR);
    }

    public void debug(String message) {
        log(message, LogLevel.DEBUG);
    }

    public void trace(String message) {
        log(message, LogLevel.TRACE);
    }

    public void success(String message) {
        log(message, LogLevel.SUCCESS);
    }

    public void failure(String message) {
        logWithoutLabel(message, LogLevel.FAILURE);
    }

    public void fatal(String message) {
        log(message, LogLevel.FATAL);
    }

    public void notice(String message) {
        log(message, LogLevel.NOTICE);
    }

    public void critical(String message) {
        log(message, LogLevel.CRITICAL);
    }

    public void alert(String message) {
        log(message, LogLevel.ALERT);
    }

    public void verbose(String message) {
        logWithoutLabel(message, LogLevel.VERBOSE);
    }

    private void log(String message, LogLevel level) {
        write(level.format(message));
    }

    private void logWithoutLabel(String message, LogLevel level) {
        write(level.defaultFormatter(message));
    }

    private synchronized void write(String message) {
        lock.lock();
        try {
            writer.println(message);
            writer.flush();
        } finally {
            lock.unlock();
        }
    }

    public void close() {
        writer.close();
    }

    /**
     * Log levels with color-coding for console output.
     */
    private enum LogLevel {
        INFO("\u001B[92m", "INFO"),               // Light Green
        USAGE("\u001B[33m", "USAGE"),             // Soft Yellow
        WARN("\u001B[38;5;214m", "WARN"),         // Amber
        ERROR("\u001B[38;5;1m", "ERROR"),         // Dark Red
        DEBUG("\u001B[38;5;32m", "DEBUG"),        // Steel Blue
        TRACE("\u001B[38;5;5m", "TRACE"),         // Purple
        SUCCESS("\u001B[38;5;2m", "SUCCESS"),     // Emerald Green
        FAILURE("\u001B[38;5;9m", "FAILURE"),     // Crimson Red
        FATAL("\u001B[97;48;5;52m", "FATAL"),     // Bright White with Dark Red Background
        NOTICE("\u001B[38;5;99m", "NOTICE"),      // Light Purple
        CRITICAL("\u001B[38;5;220m", "CRITICAL"), // Golden Yellow
        ALERT("\u001B[38;5;178m", "ALERT"),       // Deep Yellow
        VERBOSE("\u001B[96m", "VERBOSE");         // Light Cyan

        private final String colorCode;
        private final String label;

        LogLevel(String colorCode, String label) {
            this.colorCode = colorCode;
            this.label = label;
        }

        public String format(String message) {
            return String.format("%s[%s] %s\u001B[0m", colorCode, label, message); // Reset color
        }

        public String defaultFormatter(String message) {
            return String.format("%s %s\u001B[0m", colorCode, message);
        }

    }

    private static final class ShellLoggerHolder {
        private static final ShellLogger instance = new ShellLogger();
    }

}

