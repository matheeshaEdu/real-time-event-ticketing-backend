package com.iit.oop.eventticketservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class EventTicketServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(EventTicketServiceApplication.class, args);
    }

}
