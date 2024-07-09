package com.example.cardPayment.controller;

import com.example.cardPayment.dto.ProcessPaymentRequest;
import com.example.cardPayment.service.CardBalanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/card-balance")
public class CardBalanceController {

    private final CardBalanceService cardBalanceService;

    @PostMapping("/process-payment")
    public ResponseEntity<Void> processPayment(@RequestBody ProcessPaymentRequest processPaymentRequest) {
        cardBalanceService.processPayment(processPaymentRequest.getCardNumber(), processPaymentRequest.getAmount());
        return ResponseEntity.ok().build();
    }
}
