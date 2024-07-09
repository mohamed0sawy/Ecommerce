package com.example.cardValidation.client;

import com.example.cardValidation.config.PaymentClientConfiguration;
import com.example.cardValidation.dto.ProcessPaymentRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "payment-service", url = "http://localhost:8081", configuration = PaymentClientConfiguration.class)
public interface PaymentClient {
    @PostMapping("/api/v1/card-balance/process-payment")
    String processPayment(@RequestBody ProcessPaymentRequest processPaymentRequest);
}