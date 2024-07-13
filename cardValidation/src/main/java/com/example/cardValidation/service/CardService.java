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
        cardRepository.deleteAll();
    }

    public Card getCardByNumber(String cardNumberEncrypted) {
        List<Card> cards = cardRepository.findAll(); // Fetch all cards since we can't directly find by encrypted value

        Card card = cards.stream()
                .filter(c -> c.getCardNumberEncrypted().equals(cardNumberEncrypted))
                .findFirst()
                .orElseThrow(() -> ApiError.notFound("Card not found with this card number"));

        // Decrypt the card details
        try {
            card.setCardNumber(EncryptionUtils.decrypt(card.getCardNumberEncrypted()));
            card.setPin(Long.valueOf(EncryptionUtils.decrypt(card.getPinEncrypted())));
            card.setCvc(Long.valueOf(EncryptionUtils.decrypt(card.getCvcEncrypted())));
        } catch (Exception e) {
            throw new RuntimeException("Error decrypting card details", e);
        }

        return card;
    }

    public void validateCard(String cardNumberEncrypted, String pinEncrypted, String cvcEncrypted, Long expMonth, Long expYear) {
        Card card = getCardByNumber(cardNumberEncrypted);

        if (!card.getStatus().equals(CardStatus.ACTIVE)) {
            throw ApiError.badRequest("Card status is " + card.getStatus());
        }
        if (!card.getPinEncrypted().equals(pinEncrypted)) {
            System.out.println(card.getPinEncrypted());
            System.out.println(pinEncrypted);
            throw ApiError.badRequest("Pin is incorrect!");
        }
        if (!card.getCvcEncrypted().equals(cvcEncrypted)) {
            throw ApiError.badRequest("Cvc is incorrect!");
        }
        if (!card.getExpMonth().equals(expMonth)) {
            throw ApiError.badRequest("Expiry Month is incorrect!");
        }
        if (!card.getExpYear().equals(expYear)) {
            throw ApiError.badRequest("Expiry Year is incorrect!");
        }
    }

    public void validateCVC(String cardNumberEncrypted, String cvcEncrypted) {
        Card card = getCardByNumber(cardNumberEncrypted);

        if (!card.getCvcEncrypted().equals(cvcEncrypted)) {
            throw ApiError.badRequest("CVC is incorrect!");
        }
    }
}
