package com.iit.oop.eventticketservice.service.cli.menu;

import com.iit.oop.eventticketservice.service.cli.ShellProcess;
import com.iit.oop.eventticketservice.service.cli.config.GetUserConfig;
import com.iit.oop.eventticketservice.service.cli.config.SetUserConfig;
import com.iit.oop.eventticketservice.service.cli.logs.ServerLogs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.AbstractShellComponent;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;


@ShellComponent
public class ShellHandler extends AbstractShellComponent {
    private final ShellProcess serverLogs;
    private final ShellProcess userConfig;
    private final ShellProcess setConfig;

    @Autowired
    public ShellHandler(ServerLogs serverLogs, GetUserConfig userConfig, SetUserConfig setConfig) {
        this.serverLogs = serverLogs;
        this.userConfig = userConfig;
        this.setConfig = setConfig;
    }

    @ShellMethod("Tail application log")
    public void realTimeLogs() {
        serverLogs.start();
    }

    @ShellMethod("Display user configuration")
    public void showConfig() {
        userConfig.start();
    }

    @ShellMethod("Set user configuration")
    public void setUserConfig() {
        setConfig.start();
    }
}
