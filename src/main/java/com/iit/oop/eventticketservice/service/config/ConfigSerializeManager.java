package com.iit.oop.eventticketservice.service.config;

import com.google.gson.Gson;
import com.iit.oop.eventticketservice.model.TicketConfig;

public class ConfigSerializeManager {
    private final Gson gson;

    public ConfigSerializeManager(Gson gson) {
        this.gson = gson;
    }

    public String serialize(TicketConfig object) {
        return gson.toJson(object);
    }

    public TicketConfig deserialize(String json) {
        return gson.fromJson(json, TicketConfig.class);
    }
}
