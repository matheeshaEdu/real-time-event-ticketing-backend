package com.iit.oop.eventticketservice.configurations;

import com.iit.oop.eventticketservice.cli.ShellHandler;
import com.iit.oop.eventticketservice.cli.ShellManager;
import com.iit.oop.eventticketservice.cli.process.Process;
import com.iit.oop.eventticketservice.cli.process.simulation.SimulationProcess;
import com.iit.oop.eventticketservice.cli.process.simulation.StopSimulationProcess;
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
        Process simulationProcess = ApplicationContextHolder.getBean(SimulationProcess.class);
        Process stopSimulationProcess = ApplicationContextHolder.getBean(StopSimulationProcess.class);
        // Initialize and start the ShellManager
        ShellManager shellManager = startShell(simulationProcess, stopSimulationProcess);
        setObservers();
        cleanUp(shellManager);
    }

    private static ShellManager startShell(Process simulationProcess, Process stopSimulationProcess) {
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