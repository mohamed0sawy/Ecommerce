package com.academy.Ecommerce.feignClient;

import com.academy.Ecommerce.DTO.CardInfoDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "payment-service", url = "http://localhost:8081")
public interface PaymentClientService {
    @PostMapping("/api/v1/validate")
    HttpStatus payWithCard(@RequestBody CardInfoDTO cardInfoDTO);
}
