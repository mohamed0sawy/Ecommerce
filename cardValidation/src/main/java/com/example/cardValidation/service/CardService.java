package com.example.cardValidation.service;

import com.example.cardValidation.client.PaymentClient;
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
    private final PaymentClient paymentClient;

    public void saveCard(Card card) {
        cardRepository.save(card);
    }

    public void deleteAll() {
        cardRepository.deleteAll();;
    }

    public void validateCard(String cardNumber, Long pin, Long cvc, Long expMonth, Long expYear) {
        Card card = cardRepository.findByCardNumber(cardNumber);

        if(card == null) {
            throw ApiError.notFound("Card not found with this card number");
        }

        if(!card.getStatus().equals(CardStatus.ACTIVE)) {
            throw ApiError.badRequest("Card status is "+card.getStatus());
        }
        if(!card.getPin().equals(pin)) {
            throw ApiError.badRequest("Pin is incorrect!");
        }
        if(!card.getCvc().equals(cvc)) {
            throw ApiError.badRequest("Cvc is incorrect!");
        }
        if(!card.getExpMonth().equals(expMonth)) {
            throw ApiError.badRequest("Expiry Month is incorrect!");
        }
        if(!card.getExpYear().equals(expYear)) {
            throw ApiError.badRequest("Expiry Year is incorrect!");
        }
        return;
    }

    public void validateCVC(String cardNumber, Long cvc) {
        Card card = cardRepository.findByCardNumber(cardNumber);

        if(card == null) {
            throw ApiError.notFound("Card not found with this card number");
        }

        if(!card.getCvc().equals(cvc)) {
            throw ApiError.badRequest("CVC card is wrong!");
        }

    }

}
