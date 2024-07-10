package com.academy.Ecommerce.service;

import com.academy.Ecommerce.DTO.ProcessPaymentRequest;
import com.academy.Ecommerce.DTO.ValidateCVCRequest;
import com.academy.Ecommerce.feignClient.PaymentClient;
import com.academy.Ecommerce.feignClient.ValidationClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final ValidationClient validationClientService;
    private final PaymentClient paymentClient;

    public void processPayment(ValidateCVCRequest validateCVCRequest, ProcessPaymentRequest processPaymentRequest) {
        validationClientService.validateCVC(validateCVCRequest);
        paymentClient.processPayment(processPaymentRequest);
    }

}
