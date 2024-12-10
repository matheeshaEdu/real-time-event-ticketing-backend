package com.uow.eventticketservice.util.log;

import com.uow.eventticketservice.util.shell.ShellLogger;
import org.slf4j.Logger;

public class DualLogger {
    private final Logger logger;
    private final ShellLogger shellLogger;

    public DualLogger(Logger logger) {
        this.logger = logger;
        this.shellLogger = ShellLogger.getInstance();
    }

    public void logAndAlert(String message) {
        logger.info(message);
        shellLogger.alert(message);
    }

    public void logAndInfo(String message) {
        logger.info(message);
        shellLogger.info(message);
    }

    public void logAndSuccess(String message) {
        logger.info(message);
        shellLogger.success(message);
    }

    public void logAndFailure(String message) {
        logger.error(message);
        shellLogger.failure(message);
    }
}
