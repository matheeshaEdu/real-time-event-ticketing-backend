package com.iit.oop.eventticketservice.service.config;

import com.google.gson.Gson;
import com.iit.oop.eventticketservice.model.UserConfig;

public class ConfigSerializeManager {
    private final Gson gson;

    public ConfigSerializeManager(Gson gson) {
        this.gson = gson;
    }

    public String serialize(UserConfig object) {
        return gson.toJson(object);
    }

    public UserConfig deserialize(String json) {
        return gson.fromJson(json, UserConfig.class);
    }
}
