package com.iit.oop.eventticketservice.service.cli.config;

import com.iit.oop.eventticketservice.model.UserConfig;
import com.iit.oop.eventticketservice.exception.ConfigNotFoundException;
import com.iit.oop.eventticketservice.service.cli.ShellProcess;
import com.iit.oop.eventticketservice.service.config.ConfigManager;
import com.iit.oop.eventticketservice.util.logger.ShellLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GetUserConfig implements ShellProcess {
    private static final ConfigManager configManager = ConfigManager.getInstance();
    private final ShellLogger shellLogger;

    @Autowired
    public GetUserConfig(ShellLogger shellLogger) {
        this.shellLogger = shellLogger;
    }

    public void start() {
        UserConfig conf;
        try {
            conf = configManager.getConfig();
        } catch (ConfigNotFoundException e) {
            shellLogger.error("No configuration found. Please set configuration first.");
            return;
        }
        shellLogger.info(conf.toString());
    }
}
