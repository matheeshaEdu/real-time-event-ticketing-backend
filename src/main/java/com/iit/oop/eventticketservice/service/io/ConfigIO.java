package com.iit.oop.eventticketservice.service.io;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.iit.oop.eventticketservice.model.UserConfig;
import com.iit.oop.eventticketservice.service.config.ConfigSerializeManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ConfigIO {
    @Value("${userConfig.configFilePath}")
    private String configFilePath;
    private static final Logger logger = LoggerFactory.getLogger(ConfigIO.class);
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    private ConfigIO() {
    }

    private static class ConfigIOHolder {
        private static final ConfigIO INSTANCE = new ConfigIO();
    }

    public static ConfigIO getInstance() {
        return ConfigIOHolder.INSTANCE;
    }

    public void saveConfig(UserConfig config) {
        // serialize the object to JSON
        ConfigSerializeManager configSerializeManager = new ConfigSerializeManager(gson);
        String json = configSerializeManager.serialize(config);

        // write the JSON string to the file
        FileIO fileIO = new FileIO();
        try{
            fileIO.writeToFile(json, configFilePath, false);
            logger.info("Config saved successfully");
        } catch (IOException e) {
            logger.error("Error saving config", e);
        }
    }

    public UserConfig loadConfig() {
        // read the JSON string from the file
        FileIO fileIO = new FileIO();
        String json = null;
        try {
            json = fileIO.readFromFile(configFilePath);
            logger.info("Config loaded successfully");
        } catch (IOException e) {
            logger.error("Error loading config", e);
        }

        // deserialize the JSON string to an object
        ConfigSerializeManager configSerializeManager = new ConfigSerializeManager(gson);
        return configSerializeManager.deserialize(json);
    }
}
