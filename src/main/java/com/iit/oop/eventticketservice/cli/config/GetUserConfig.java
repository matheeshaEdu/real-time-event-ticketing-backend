package com.iit.oop.eventticketservice.cli.config;

import com.iit.oop.eventticketservice.model.UserConfig;
import com.iit.oop.eventticketservice.exception.ConfigNotFoundException;
import com.iit.oop.eventticketservice.cli.ShellProcess;
import com.iit.oop.eventticketservice.service.config.ConfigManager;
import com.iit.oop.eventticketservice.util.shell.ShellLogger;
import org.springframework.stereotype.Component;

@Component
public class GetUserConfig implements ShellProcess {
    private final ConfigManager configManager = ConfigManager.getInstance();
    private final ShellLogger shellLogger = ShellLogger.getInstance();

    public void start() {
        UserConfig conf;
        shellLogger.info(Thread.currentThread().getName());
        try {
            conf = configManager.getConfig();
        } catch (ConfigNotFoundException e) {
            shellLogger.error("No configuration found. Please set configuration first.");
            return;
        }
        shellLogger.info(conf.toString());
    }
}
