package com.academy.Ecommerce.controller;


import com.academy.Ecommerce.dto.BalanceDto;
import com.academy.Ecommerce.service.PaymentService;
import com.stripe.exception.StripeException;
import com.stripe.model.Balance;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/payments")
public class PaymentController {

    private final PaymentService paymentService;

    @GetMapping("/balance")
    private ResponseEntity<BalanceDto> getBalance() {
        try {
            BalanceDto balance = paymentService.retrieveBalance();
            return ResponseEntity.ok(balance);
        } catch (StripeException e) {
            return ResponseEntity.status(500).body(null);
        }
    }
}
