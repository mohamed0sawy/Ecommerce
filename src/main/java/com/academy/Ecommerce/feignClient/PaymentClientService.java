package com.academy.Ecommerce.feignClient;

import com.academy.Ecommerce.DTO.CardInfoDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(url = "http://localhost:8081")
public interface PaymentClientService {
    @PostMapping("/api/v1/validate")
    boolean payWithCard(@RequestBody CardInfoDTO cardInfoDTO);
}
