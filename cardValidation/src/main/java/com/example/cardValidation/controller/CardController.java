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
@RequestMapping("/api/v1/cardValidation")
public class CardController {

    private final CardService cardService;

    @PostMapping("/validateCard")
    public ResponseEntity<Void> validateCard(@RequestBody ValidationRequest validationRequest) {
        cardService.validateCard(validationRequest.getCardNumberEncrypted(),
                validationRequest.getPinEncrypted(),
                validationRequest.getCvcEncrypted(),
                validationRequest.getExpMonth(),
                validationRequest.getExpYear());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/validateCVC")
    public ResponseEntity<Void> validateCVC(@RequestBody ValidateCVCRequest validateCVCDto) {
        cardService.validateCVC(validateCVCDto.getCardNumberEncrypted(), validateCVCDto.getCvcEncrypted());
        return ResponseEntity.ok().build();
    }
}
