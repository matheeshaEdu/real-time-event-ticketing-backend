package com.uow.eventticketservice.controller.transaction;

import com.uow.eventticketservice.dto.response.ResponseMessageDto;
import com.uow.eventticketservice.service.transaction.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/transaction")
public class TransactionController {
    private final TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping("/topVendors")
    public ResponseEntity<Object> getTopVendors() {
        try {
            return ResponseEntity.ok(transactionService.getTopVendors());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                   new ResponseMessageDto ("400 Bad Request", e.getMessage())
            );
        }
    }
}
