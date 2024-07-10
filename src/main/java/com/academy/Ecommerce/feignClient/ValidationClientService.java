package com.academy.Ecommerce.feignClient;

import com.academy.Ecommerce.DTO.ValidateCVCRequest;
import com.academy.Ecommerce.DTO.ValidationRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "validation-service", url = "http://localhost:8081/api/v1/cardValidation")
public interface ValidationClientService {
    @PostMapping("/validateCard")
    ResponseEntity<Void> validateCard(@RequestBody ValidationRequest cardInfoDTO);

    @PostMapping("/validateCVC")
    ResponseEntity<Void> validateCVC(@RequestBody ValidateCVCRequest validateCVCRequest);
}
