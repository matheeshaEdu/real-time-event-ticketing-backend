package com.iit.oop.eventticketservice.service.config;

import com.iit.oop.eventticketservice.model.UserConfig;
import com.iit.oop.eventticketservice.exception.ConfigNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserConfigService {
    private final ConfigManager configManager = ConfigManager.getInstance();

    public UserConfig getConfig() throws ConfigNotFoundException {
        return configManager.getConfig();
    }

    public void setConfig(UserConfig userConfig) {
        configManager.setUserConfig(userConfig);
    }
}
