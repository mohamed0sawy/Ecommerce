package com.academy.Ecommerce.feignClient;

import com.academy.Ecommerce.DTO.ProcessPaymentRequest;
import com.academy.Ecommerce.DTO.ProcessPaymentRequestEnc;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "payment-service", url = "http://localhost:8082/api/v1/cardPayment")
public interface PaymentClient {
    @PostMapping("/processPayment")
    ResponseEntity<Void> processPayment(@RequestBody ProcessPaymentRequestEnc processPaymentRequest);
}