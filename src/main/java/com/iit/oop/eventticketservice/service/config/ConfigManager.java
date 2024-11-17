package com.iit.oop.eventticketservice.service.config;

import com.iit.oop.eventticketservice.model.UserConfig;
import com.iit.oop.eventticketservice.exception.ConfigNotFoundException;
import com.iit.oop.eventticketservice.service.io.ConfigIO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConfigManager {

    private static  final Logger logger = LoggerFactory.getLogger(ConfigManager.class);
    private static final ConfigIO configIO = ConfigIO.getInstance();
    private UserConfig config;

    // Private constructor to prevent instantiation from outside
    private ConfigManager() {
    }


    // Static inner helper class responsible for holding the singleton instance
    private static class ConfigManagerHolder {
        private static final ConfigManager INSTANCE = new ConfigManager();
    }

    // Public method to provide access to the singleton instance
    public static ConfigManager getInstance() {
        return ConfigManagerHolder.INSTANCE;
    }

    // Instance method to get configuration, initializes if needed
    public UserConfig getConfig() throws ConfigNotFoundException {
        if (config == null) {
            logger.info("Loading user config from file");
            config = configIO.loadConfig();
        }
        if (config == null) {
            throw new ConfigNotFoundException("User Configs are not defined");
        }
        logger.info("User config found");
        return config;
    }

    // Instance method to set configuration and save it
    public void setUserConfig(UserConfig userConfig) {
        config = userConfig;
        configIO.saveConfig(userConfig);
    }
}
