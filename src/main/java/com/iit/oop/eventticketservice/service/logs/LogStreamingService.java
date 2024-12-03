package com.iit.oop.eventticketservice.service.logs;

import com.iit.oop.eventticketservice.util.Global;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.RandomAccessFile;

@Service
public class LogStreamingService {
    private static final Logger log = LoggerFactory.getLogger(LogStreamingService.class);
    private final SimpMessagingTemplate messagingTemplate;
    private long lastReadPosition = 0;

    public LogStreamingService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @Scheduled(fixedRate = 1000) // Check the log file every second
    public void streamLogs() {
        try (RandomAccessFile logFile = new RandomAccessFile(Global.LOG_PATH, "r")) {
            logFile.seek(lastReadPosition);
            String line;
            while ((line = logFile.readLine()) != null) {
                messagingTemplate.convertAndSend(Global.WS_TOPIC_LOGS, line);
            }
            lastReadPosition = logFile.getFilePointer();
        } catch (IOException e) {
            log.error("Error reading log file: {}", e.getMessage());
        }
    }
}

