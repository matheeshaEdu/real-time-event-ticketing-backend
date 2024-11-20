package com.iit.oop.eventticketservice.service.config;

import com.iit.oop.eventticketservice.model.UserConfig;
import com.iit.oop.eventticketservice.exception.ConfigNotFoundException;
import com.iit.oop.eventticketservice.service.io.ConfigIO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConfigManager {

    private static final Logger logger = LoggerFactory.getLogger(ConfigManager.class);

    // The configuration object (volatile ensures visibility across threads)
    private volatile UserConfig config;

    // Dependency for config I/O operations, injected via constructor
    private final ConfigIO configIO;

    // Private constructor to allow dependency injection and prevent direct instantiation
    private ConfigManager(ConfigIO configIO) {
        this.configIO = configIO;
    }

    // Static inner class for lazy-loaded singleton
    private static class ConfigManagerHolder {
        private static final ConfigManager INSTANCE = new ConfigManager(ConfigIO.getInstance());
    }

    // Public method to get the singleton instance
    public static ConfigManager getInstance() {
        return ConfigManagerHolder.INSTANCE;
    }

    /**
     * Retrieves the configuration. Initializes lazily if not already loaded.
     *
     * @return UserConfig The user configuration object.
     * @throws ConfigNotFoundException if configuration is missing.
     */
    public UserConfig getConfig() throws ConfigNotFoundException {
        if (this.config == null) { // First check (without locking)
            synchronized (this) {
                if (this.config == null) { // Second check (with locking)
                    logger.info("Loading user config from file...");
                    UserConfig loadedConfig = configIO.loadConfig();
                    if (loadedConfig == null) {
                        throw new ConfigNotFoundException("User Configs are not defined");
                    }
                    this.config = loadedConfig;
                }
            }
        }
        logger.info("User config successfully loaded.");
        return this.config;
    }

    /**
     * Updates the user configuration and saves it to the persistent storage.
     *
     * @param userConfig The new user configuration.
     */
    public void setUserConfig(UserConfig userConfig) {
        synchronized (this) { // Ensure thread safety for write operations
            config = userConfig;
            logger.info("Saving user config to file...");
            configIO.saveConfig(userConfig);
            logger.info("User config successfully saved.");
        }
    }
}

