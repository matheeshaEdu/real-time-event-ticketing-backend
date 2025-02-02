package com.uow.eventticketservice.util.io;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.uow.eventticketservice.model.TicketConfig;
import com.uow.eventticketservice.core.config.ConfigSerializeManager;
import com.uow.eventticketservice.util.Global;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;


public class ConfigIO {
    private static final Logger logger = LoggerFactory.getLogger(ConfigIO.class);
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    private ConfigIO() {
    }

    public static ConfigIO getInstance() {
        return ConfigIOHolder.INSTANCE;
    }

    public void saveConfig(TicketConfig config) {
        // serialize the object to JSON
        ConfigSerializeManager configSerializeManager = new ConfigSerializeManager(gson);
        String json = configSerializeManager.serialize(config);

        // write the JSON string to the file
        FileIO fileIO = new FileIO();
        try {
            fileIO.writeToFile(json, Global.CONFIG_PATH, false);
            logger.info("Config saved successfully");
        } catch (IOException e) {
            logger.error("Error saving config", e);
        }
    }

    public TicketConfig loadConfig() {
        // read the JSON string from the file
        FileIO fileIO = new FileIO();
        String json = null;
        try {
            json = fileIO.readFromFile(Global.CONFIG_PATH);
            logger.info("Config loaded successfully");
        } catch (IOException e) {
            logger.error("Error loading config", e);
        }

        // deserialize the JSON string to an object
        ConfigSerializeManager configSerializeManager = new ConfigSerializeManager(gson);
        return configSerializeManager.deserialize(json);
    }

    private static class ConfigIOHolder {
        private static final ConfigIO INSTANCE = new ConfigIO();
    }
}
