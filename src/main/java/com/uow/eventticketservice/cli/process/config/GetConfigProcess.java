package com.uow.eventticketservice.cli.process.config;

import com.uow.eventticketservice.cli.process.Process;
import com.uow.eventticketservice.exception.ConfigNotFoundException;
import com.uow.eventticketservice.model.TicketConfig;
import com.uow.eventticketservice.service.config.ConfigManager;
import com.uow.eventticketservice.util.shell.ShellLogger;

public class GetConfigProcess implements Process {
    private final ConfigManager configManager;
    private final ShellLogger shellLogger ;

    public GetConfigProcess(ConfigManager configManager, ShellLogger shellLogger) {
        this.configManager = configManager;
        this.shellLogger = shellLogger;
    }

    public void execute() {
        TicketConfig conf;
        try {
            conf = configManager.getConfig();
        } catch (ConfigNotFoundException e) {
            shellLogger.error("No configuration found. Please set configuration first.");
            return;
        }
        shellLogger.verbose(conf.toString());
    }
}
