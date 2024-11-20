package com.iit.oop.eventticketservice.cli.logs;

import com.iit.oop.eventticketservice.cli.ShellProcess;
import com.iit.oop.eventticketservice.util.Global;
import com.iit.oop.eventticketservice.util.shell.ShellLogger;
import com.iit.oop.eventticketservice.util.shell.ShellScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Shell process to tail the server log file.
 */
@Component
public class ServerLogs implements ShellProcess {
    private static final Logger log = LoggerFactory.getLogger(ServerLogs.class);
    private final ShellScanner scanner = ShellScanner.getInstance();
    private final ShellLogger shellLogger;
    private volatile boolean running; // Volatile to ensure visibility across threads
    private final Object lock = new Object(); // Synchronization lock

    public ServerLogs() {
        // Get ShellLogger instance
        this.shellLogger= ShellLogger.getInstance();
    }

    @Override
    public void start() {
        running = true;
        Thread logReaderThread = new Thread(this::readLog, "LogReaderThread");
        Thread userInputListenerThread = new Thread(this::listenForUserInput, "UserInputListenerThread");

        userInputListenerThread.start();
        logReaderThread.start();

        // Wait for threads to terminate
        try {
            logReaderThread.join();
            userInputListenerThread.join();
        } catch (InterruptedException e) {
            log.error("Error while waiting for threads to terminate", e);
            Thread.currentThread().interrupt();
        }
    }

    private void readLog() {
        Process process = null;
        try (BufferedReader reader = createLogReader()) {
            process = startLogTailProcess();
            String line;

            while (running) {
                if (reader.ready() && (line = reader.readLine()) != null) {
                    shellLogger.info(line);
                } else {
                    Thread.sleep(100); // Avoid busy-waiting
                }
            }
        } catch (IOException | InterruptedException e) {
            if (!running) {
                log.info("LogReaderThread interrupted during shutdown");
            } else {
                log.error("Error in LogReaderThread", e);
            }
        } finally {
            if (process != null) {
                process.destroy();
                log.info("Log tail process terminated.");
            }
        }
    }

    private void listenForUserInput() {
        shellLogger.usageInfo("Press 'q' to stop tailing the log and return to the shell prompt.");
            while (running) {
                String input = "";
                try {
                    input = scanner.scanString();
                    if (input.equals("q")) {
                        break;
                    }
                } catch (IllegalArgumentException e) {
                    shellLogger.error(e.getMessage());
                }
            }
            stop();
    }

    private BufferedReader createLogReader() throws IOException {
        return new BufferedReader(new InputStreamReader(startLogTailProcess().getInputStream()));
    }

    private Process startLogTailProcess() throws IOException {
        ProcessBuilder processBuilder = new ProcessBuilder("tail", "-f", Global.LOG_PATH);
        return processBuilder.start();
    }

    /**
     * Gracefully stops all running threads and resources.
     */
    public void stop() {
        synchronized (lock) {
            if (running) {
                running = false;
                log.info("Stopping log reader and user input listener...");
            }
        }
    }
}
