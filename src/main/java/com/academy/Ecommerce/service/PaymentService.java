package com.academy.Ecommerce.service;

import com.academy.Ecommerce.DTO.ProcessPaymentRequest;
import com.academy.Ecommerce.DTO.ProcessPaymentRequestEnc;
import com.academy.Ecommerce.DTO.ValidateCVCRequest;
import com.academy.Ecommerce.DTO.ValidateCVCRequestEnc;
import com.academy.Ecommerce.feignClient.PaymentClient;
import com.academy.Ecommerce.feignClient.ValidationClient;
import com.academy.Ecommerce.utility.EncryptionUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final ValidationClient validationClientService;
    private final PaymentClient paymentClient;

    public void processPayment(ValidateCVCRequest validateCVCRequest, ProcessPaymentRequest processPaymentRequest) throws Exception {
        ValidateCVCRequestEnc validateCVCRequestEnc = new ValidateCVCRequestEnc(EncryptionUtils.encrypt(validateCVCRequest.getCardNumber()),
                EncryptionUtils.encrypt(validateCVCRequest.getCVC().toString()));

        validationClientService.validateCVC(validateCVCRequestEnc);


        ProcessPaymentRequestEnc processPaymentRequest1 = new ProcessPaymentRequestEnc(EncryptionUtils.encrypt(processPaymentRequest.getCardNumber()),
                processPaymentRequest.getAmount());
        paymentClient.processPayment(processPaymentRequest1);
    }

}
