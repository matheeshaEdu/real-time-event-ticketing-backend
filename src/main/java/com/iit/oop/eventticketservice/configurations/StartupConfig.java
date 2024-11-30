package com.iit.oop.eventticketservice.configurations;

import com.iit.oop.eventticketservice.cli.ShellHandler;
import com.iit.oop.eventticketservice.cli.ShellManager;
import com.iit.oop.eventticketservice.cli.shellprocess.ShellProcess;
import com.iit.oop.eventticketservice.cli.shellprocess.simulation.SimulationProcess;
import com.iit.oop.eventticketservice.cli.shellprocess.simulation.StopSimulationProcess;
import com.iit.oop.eventticketservice.event.ObserverInitializer;
import com.iit.oop.eventticketservice.service.config.ConfigManager;
import com.iit.oop.eventticketservice.util.shell.ShellLogger;
import com.iit.oop.eventticketservice.util.spring.ApplicationContextHolder;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StartupConfig {

    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
        ShellProcess simulationProcess = ApplicationContextHolder.getBean(SimulationProcess.class);
        ShellProcess stopSimulationProcess = ApplicationContextHolder.getBean(StopSimulationProcess.class);
        // Initialize and start the ShellManager
        ShellManager shellManager = startShell(simulationProcess, stopSimulationProcess);
        setObservers();
        cleanUp(shellManager);
    }

    private static ShellManager startShell(ShellProcess simulationProcess, ShellProcess stopSimulationProcess) {
        ShellManager shellManager = new ShellManager(new ShellHandler(
                simulationProcess, stopSimulationProcess));
        shellManager.start();
        return shellManager;
    }

    private static void cleanUp(ShellManager shellManager) {
        // Add shutdown hooks for resource cleanup
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            shellManager.stop();
            ShellLogger.getInstance().close();
        }));
    }

    private static void setObservers() {
        // set config observer
        ConfigManager.getInstance().setObservers(
                List.of(
                        ObserverInitializer.getInstance().getConfigObserver()
                )
        );
    }
}