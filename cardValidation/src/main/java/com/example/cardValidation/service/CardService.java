package com.example.cardValidation.service;

import com.example.cardValidation.client.PaymentClient;
import com.example.cardValidation.exception.ApiError;
import com.example.cardValidation.model.Card;
import com.example.cardValidation.model.CardStatus;
import com.example.cardValidation.repository.CardRepository;
import com.example.cardValidation.utility.EncryptionUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class CardService {

    private final CardRepository cardRepository;
    private final PaymentClient paymentClient;

    public void saveCard(Card card) {
        try {
            card.setCardNumberEncrypted(EncryptionUtils.encrypt(card.getCardNumber()));
            card.setPinEncrypted(EncryptionUtils.encrypt(card.getPin().toString()));
            card.setCvcEncrypted(EncryptionUtils.encrypt(card.getCvc().toString()));
            cardRepository.save(card);
        } catch (Exception e) {
            throw new RuntimeException("Error encrypting card details", e);
        }
    }
    public void deleteAll() {
        cardRepository.deleteAll();;
    }

    public Card getCardByNumber(String cardNumber) {
        try {
            List<Card> cards = cardRepository.findAll(); // Fetch all cards since we can't directly find by encrypted value

            Card card = cards.stream()
                    .filter(c -> {
                        try {
                            return EncryptionUtils.decrypt(c.getCardNumberEncrypted()).equals(cardNumber);
                        } catch (Exception e) {
                            throw ApiError.notFound("Error while decrypting");
                        }
                    })
                    .findFirst()
                    .orElseThrow(() -> ApiError.notFound("Card not found with this card number"));

            card.setCardNumber(EncryptionUtils.decrypt(card.getCardNumberEncrypted()));
            card.setPin(Long.valueOf(EncryptionUtils.decrypt(card.getPinEncrypted())));
            card.setCvc(Long.valueOf(EncryptionUtils.decrypt(card.getCvcEncrypted())));

            return card;
        } catch (Exception e) {
            throw ApiError.notFound("Card not found with this card number");
        }
    }

    public void validateCard(String cardNumber, Long pin, Long cvc, Long expMonth, Long expYear) {
        Card card = getCardByNumber(cardNumber);

        if (!card.getStatus().equals(CardStatus.ACTIVE)) {
            throw ApiError.badRequest("Card status is " + card.getStatus());
        }
        if (!card.getPin().equals(pin)) {
            throw ApiError.badRequest("Pin is incorrect!");
        }
        if (!card.getCvc().equals(cvc)) {
            throw ApiError.badRequest("Cvc is incorrect!");
        }
        if (!card.getExpMonth().equals(expMonth)) {
            throw ApiError.badRequest("Expiry Month is incorrect!");
        }
        if (!card.getExpYear().equals(expYear)) {
            throw ApiError.badRequest("Expiry Year is incorrect!");
        }
    }

    public void validateCVC(String cardNumber, Long cvc) {
        Card card = getCardByNumber(cardNumber);

        if (!card.getCvc().equals(cvc)) {
            throw ApiError.badRequest("CVC card is wrong!");
        }
    }

}
