package com.iit.oop.eventticketservice.controller.config;

import com.iit.oop.eventticketservice.dto.response.ResponseMessageDto;
import com.iit.oop.eventticketservice.model.TicketConfig;
import com.iit.oop.eventticketservice.service.config.UserConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/userConfig")
public class UserConfigController {
    private static final Logger log = LoggerFactory.getLogger(UserConfigController.class);
    private final UserConfigService userConfigService;

    @Autowired
    public UserConfigController(UserConfigService userConfigService) {
        this.userConfigService = userConfigService;
    }

    @GetMapping("/get")
    public ResponseEntity<Object> getConfig() {
        try {
            return ResponseEntity.ok(userConfigService.getConfig());
        } catch (Exception e) {
            log.error("Error getting user config", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ResponseMessageDto("400 Bad Request", "Config not found"));
        }
    }

    @PostMapping("/set")
    public ResponseEntity<Object> setConfig(@RequestBody TicketConfig ticketConfig) {
        try {
            userConfigService.setConfig(ticketConfig);
            log.info("User config updated successfully");
            return ResponseEntity.ok(new ResponseMessageDto("200 OK", "User config updated successfully"));
        } catch (Exception e) {
            log.error("Error setting user config", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ResponseMessageDto("400 Bad Request", "Could not update user config"));
        }

    }

}
