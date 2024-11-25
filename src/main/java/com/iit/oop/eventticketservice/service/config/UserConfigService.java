package com.iit.oop.eventticketservice.service.config;

import com.iit.oop.eventticketservice.exception.ConfigNotFoundException;
import com.iit.oop.eventticketservice.model.TicketConfig;
import org.springframework.stereotype.Service;

@Service
public class UserConfigService {
    private final ConfigManager configManager = ConfigManager.getInstance();

    public TicketConfig getConfig() throws ConfigNotFoundException {
        return configManager.getConfig();
    }

    public void setConfig(TicketConfig ticketConfig) {
        configManager.setUserConfig(ticketConfig);
    }
}
