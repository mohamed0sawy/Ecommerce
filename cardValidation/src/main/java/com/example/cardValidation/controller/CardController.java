package com.example.cardValidation.controller;

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

    @PostMapping("/validate")
    public ResponseEntity<String> validateCard(@RequestBody ValidationRequest validationRequest) {
        if( cardService.validateCard(
                validationRequest.getNumber(),
                validationRequest.getPin(),
                validationRequest.getCvc(),
                validationRequest.getExpMonth(),
                validationRequest.getExpYear())) {
            return ResponseEntity.ok("Card is valid");
        }
        else {
            return ResponseEntity.badRequest().body("Card is not valid");
        }
    }
}
