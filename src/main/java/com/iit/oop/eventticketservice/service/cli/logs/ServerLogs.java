package com.iit.oop.eventticketservice.service.cli.logs;

import com.iit.oop.eventticketservice.service.cli.ShellProcess;
import com.iit.oop.eventticketservice.util.logger.ShellLogger;
import org.jline.terminal.Terminal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;

@Component
public class ServerLogs implements ShellProcess {
    private static final Logger log = LoggerFactory.getLogger(ServerLogs.class);
    private final ShellLogger shellLogger;
    @Value("${logging.file.name}")
    private String logPath;

    public ServerLogs(Terminal terminal) {
        this.shellLogger = new ShellLogger(terminal);
    }

    @Override
    public void start() {
        Thread logReader = new Thread(this::viewLog);
        logReader.setDaemon(true);
        logReader.start();
        // Wait for user to stop log tailing
        stop(logReader);
    }

    private void viewLog() {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("tail", "-f", logPath);
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                if(Thread.currentThread().isInterrupted()){
                    break;
                }
                shellLogger.info(line);
            }
            // clean up
            reader.close();
            process.destroy();
        } catch (Exception e) {
            log.error("Error reading log file", e);
        }
    }

    private void stop(Thread thread) {
        shellLogger.usageInfo("Press 'q' to stop tailing the log and return to the shell prompt.");
        try {
            while (true) {
                int key = System.in.read();
                if (key == 'q') { // 'q' is implicitly converted to its ASCII value 113
                    thread.interrupt();
                    break;
                }
            }
        } catch (Exception e) {
            log.error("Error stopping log tailing: {}", e.getMessage());
        }
    }

}
