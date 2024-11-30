package com.iit.oop.eventticketservice.service.config;

import com.iit.oop.eventticketservice.event.observer.DomainEventObserver;
import com.iit.oop.eventticketservice.event.subject.DomainEventPublisher;
import com.iit.oop.eventticketservice.event.subject.Subject;
import com.iit.oop.eventticketservice.exception.ConfigNotFoundException;
import com.iit.oop.eventticketservice.model.TicketConfig;
import com.iit.oop.eventticketservice.util.io.ConfigIO;
import com.iit.oop.eventticketservice.util.log.DualLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class ConfigManager {

    private static final Logger logger = LoggerFactory.getLogger(ConfigManager.class);
    private final DualLogger dualLogger;
    private final AtomicReference<TicketConfig> config; // Thread-safe reference to user configuration
    private final ConfigIO configIO;
    private final Subject<TicketConfig> subject;

    // Private constructor to allow dependency injection and prevent direct instantiation
    private ConfigManager(ConfigIO configIO) {
        this.configIO = configIO;
        this.subject = new DomainEventPublisher<>();
        this.config = new AtomicReference<>();
        this.dualLogger = new DualLogger(logger);
    }

    // Public method to get the singleton instance
    public static ConfigManager getInstance() {
        return ConfigManagerHolder.INSTANCE;
    }

    public void setObservers(List<DomainEventObserver<TicketConfig>> observers) {
        for (DomainEventObserver<TicketConfig> observer : observers) {
            subject.addObserver(observer);
        }
    }

    /**
     * Retrieves the configuration. Initializes lazily if not already loaded.
     *
     * @return UserConfig The user configuration object.
     * @throws ConfigNotFoundException if configuration is missing.
     */
    public TicketConfig getConfig() throws ConfigNotFoundException {
        if (this.config.get() == null) { // First check (without locking)
            synchronized (this) {
                if (this.config.get() == null) { // Second check (with locking)
                    logger.info("Loading user config from file...");
                    TicketConfig loadedConfig = configIO.loadConfig();
                    if (loadedConfig == null) {
                        throw new ConfigNotFoundException("User Configs are not defined. Please set the user configs.");
                    }
                    this.config.set(loadedConfig);
                }
            }
        }
        return this.config.get();
    }

    /**
     * Updates the user configuration and saves it to the persistent storage.
     *
     * @param config The new user configuration.
     */
    public void setUserConfig(TicketConfig config) {
        synchronized (this) {// Ensure thread safety for write operations
            TicketConfig currentConfig = this.config.get();
            if (currentConfig != null) {
                if (currentConfig.equals(config)) {
                    dualLogger.logAndInfo("User config is already up-to-date. No need to save.");
                    return;
                }
                dualLogger.logAndAlert("Updating user config...");
                this.config.set(config);
                subject.notifyObservers(this.config.get());
                return;
            }

            this.config.set(config);
            logger.info("Saving user config to file...");
            configIO.saveConfig(config);
            dualLogger.logAndSuccess( "User config successfully saved...");
        }
    }

    // Static inner class for lazy-loaded singleton
    private static class ConfigManagerHolder {
        private static final ConfigManager INSTANCE = new ConfigManager(ConfigIO.getInstance());
    }
}

