package com.iit.oop.eventticketservice.util.shell;

import java.io.PrintWriter;

/**
 * A thread-safe shell logger with configurable log levels and output mechanisms.
 */
public class ShellLogger implements ShellClosable {

    private final PrintWriter writer = new PrintWriter(System.out);

    // Private constructor for Singleton
    private ShellLogger() {
    }

    private static final class ShellLoggerHolder {
        private static final ShellLogger instance = new ShellLogger();
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
        logWithoutLabel(message);
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

    private void log(String message, LogLevel level) {
            write(level.format(message));
    }

    private void logWithoutLabel(String message) {
        write(LogLevel.USAGE_INFO.defaultFormatter(message));
    }

    private synchronized void write(String message) {
        writer.println(message);
        writer.flush();
    }

    /**
     * Log levels with color-coding for console output.
     */
    public enum LogLevel {
        INFO("\u001B[32m", "INFO"),         // Green
        USAGE_INFO("\u001B[1;36m", "USAGE"), // Bold Cyan
        WARN("\u001B[33m", "WARN"),         // Yellow
        ERROR("\u001B[31m", "ERROR"),       // Red
        DEBUG("\u001B[34m", "DEBUG");      // Blue

        private final String colorCode;
        private final String label;

        LogLevel(String colorCode, String label) {
            this.colorCode = colorCode;
            this.label = label;
        }

        public String format(String message) {
            return String.format("%s[%s] %s\u001B[0m", colorCode, label, message); // Reset color
        }

        public String defaultFormatter(String message){
            return String.format("%s %s\u001B[0m", colorCode, message);
        }

    }

    public void close() {
            writer.close();
    }

}

