package com.example.cardValidation.service;

import com.example.cardValidation.exception.ApiError;
import com.example.cardValidation.model.Card;
import com.example.cardValidation.model.CardStatus;
import com.example.cardValidation.repository.CardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class CardService {

    private final CardRepository cardRepository;

    public boolean validateCard(String cardNumber, Long pin, Long cvc, Long expMonth, Long expYear) {
        Card card = cardRepository.findByNumber(cardNumber);

        if(card == null) {
            System.out.println("card null");
            throw ApiError.notFound("Card not found with this card number");
        }
        System.out.println("card is found");

        if(!card.getStatus().equals(CardStatus.ACTIVE)) {
            System.out.println("card status is inactive");
            throw ApiError.badRequest("Card status is "+card.getStatus());
        }

        boolean isValid = card.getPin().equals(pin)
                && card.getCvc().equals(cvc)
                && card.getExpMonth().equals(expMonth)
                && card.getExpYear().equals(expYear);

        System.out.println("Validation result: " + isValid);
        return isValid;
    }

}
