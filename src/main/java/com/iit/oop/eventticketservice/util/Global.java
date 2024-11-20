package com.iit.oop.eventticketservice.util;

public final class Global {
    // Prevent instantiation
    private Global() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    // Define constants
    public static final String APP_NAME = "EventTicketService";
    public static final String VERSION = "1.0.0";
    public static final int MAX_RETRIES = 3;
    public static final String LOG_PATH = "src/main/resources/logs/application.log";
    public static final String CONFIG_PATH = "src/main/resources/config/userConfig.json";
    // Add more constants as needed
}
