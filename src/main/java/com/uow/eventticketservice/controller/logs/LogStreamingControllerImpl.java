package com.uow.eventticketservice.controller.logs;

import com.uow.eventticketservice.dto.response.LogMessage;
import com.uow.eventticketservice.util.Global;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.RandomAccessFile;

/*
 * This class is responsible for streaming log messages to the WebSocket clients.
 */
@Service
public class LogStreamingControllerImpl implements LogStreamingService {
    private static final Logger log = LoggerFactory.getLogger(LogStreamingControllerImpl.class);
    private final SimpMessagingTemplate messagingTemplate;
    private long lastReadPosition = 0;

    public LogStreamingControllerImpl(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    /**
     * Stream log messages to the WebSocket clients
     * This method is scheduled to run every second
     * It reads the log file and sends the new log messages to the clients
     */
    @Override
    @Scheduled(fixedRate = 1000) // Check the log file every second
    public void streamLogs() {
        try (RandomAccessFile logFile = new RandomAccessFile(Global.LOG_PATH, "r")) {
            logFile.seek(lastReadPosition);
            String line;
            while ((line = logFile.readLine()) != null) {
                LogMessage logMessage = new LogMessage(line);
                sendLogMessage(logMessage);
            }
            lastReadPosition = logFile.getFilePointer();
        } catch (IOException e) {
            log.error("Error reading log file: {}", e.getMessage());
        }
    }

    /**
     * Send the log message to the WebSocket clients
     * @param logMessage LogMessage instance
     */
    private void sendLogMessage(LogMessage logMessage) {
        new Thread(() -> messagingTemplate.convertAndSend(Global.WS_TOPIC_LOGS, logMessage)).start();
    }
}