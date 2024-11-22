package com.iit.oop.eventticketservice.cli.shellprocess.config;

import com.iit.oop.eventticketservice.cli.shellprocess.ShellProcess;
import com.iit.oop.eventticketservice.model.UserConfig;
import com.iit.oop.eventticketservice.exception.ConfigNotFoundException;
import com.iit.oop.eventticketservice.service.config.ConfigManager;
import com.iit.oop.eventticketservice.util.shell.ShellLogger;

public class GetConfigProcess implements ShellProcess {
    private final ConfigManager configManager = ConfigManager.getInstance();
    private final ShellLogger shellLogger = ShellLogger.getInstance();

    public void execute() {
        UserConfig conf;
        try {
            conf = configManager.getConfig();
        } catch (ConfigNotFoundException e) {
            shellLogger.error("No configuration found. Please set configuration first.");
            return;
        }
        shellLogger.verbose(conf.toString());
    }
}
