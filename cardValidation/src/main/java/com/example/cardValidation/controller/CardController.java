package com.example.cardValidation.controller;

import com.example.cardValidation.dto.ValidateCVCRequest;
import com.example.cardValidation.dto.ValidationRequest;
import com.example.cardValidation.service.CardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/card-validation")
public class CardController {

    private final CardService cardService;

    @PostMapping("/validate-card")
    public ResponseEntity<Void> validateCard(@RequestBody ValidationRequest validationRequest) {
        cardService.validateCard(validationRequest.getNumber(),
                validationRequest.getPin(),
                validationRequest.getCvc(),
                validationRequest.getExpMonth(),
                validationRequest.getExpYear());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/validate-cvc")
    public ResponseEntity<Void> validateCVC(@RequestBody ValidateCVCRequest validateCVCDto) {
        cardService.validateCVC(validateCVCDto.getCardNumber(), validateCVCDto.getCVC());
        return ResponseEntity.ok().build();
    }
}
