package com.example.cardPayment.service;

import com.example.cardPayment.exception.ApiError;
import com.example.cardPayment.model.CardBalance;
import com.example.cardPayment.repository.CardBalanceRepository;
import com.example.cardPayment.utility.EncryptionUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CardBalanceService {

    private final CardBalanceRepository cardBalanceRepository;
    private static final Logger logger = LoggerFactory.getLogger(CardBalanceService.class);

    public void saveCardBalance(CardBalance cardBalance) {
        try {
            String encryptedCardNumber = EncryptionUtils.encrypt(cardBalance.getCardNumber());
            cardBalance.setCardNumberEncrypted(encryptedCardNumber);
            cardBalanceRepository.save(cardBalance);
        } catch (Exception e) {
            logger.error("Error encrypting card number for card: " + cardBalance.getCardNumber(), e);
            throw new RuntimeException("Error encrypting card number", e);
        }
    }

    public void deleteCardBalances() {
        cardBalanceRepository.deleteAll();
    }

    private CardBalance getCardBalanceByCardNumber(String cardNumber) {
        try {
            List<CardBalance> cardBalances = cardBalanceRepository.findAll(); // Fetch all since we can't directly find by encrypted value

            CardBalance cardBalance = cardBalances.stream()
                    .filter(c -> {
                        try {
                            return EncryptionUtils.decrypt(c.getCardNumberEncrypted()).equals(cardNumber);
                        } catch (Exception e) {
                            logger.error("Error decrypting card number for card: " + c.getCardNumberEncrypted(), e);
                            return false; // Filter out this card if decryption fails
                        }
                    })
                    .findFirst()
                    .orElseThrow(() -> ApiError.notFound("Card not found with this card number"));

            cardBalance.setCardNumber(EncryptionUtils.decrypt(cardBalance.getCardNumberEncrypted()));

            return cardBalance;
        } catch (Exception e) {
            throw ApiError.notFound("Card not found with this card number");
        }
    }

    @Transactional
    public void processPayment(String cardNumber, Long amount) {
        CardBalance cardBalance = getCardBalanceByCardNumber(cardNumber);
        Long balance = cardBalance.getBalance();
        if (balance < amount) {
            throw ApiError.badRequest("Insufficient funds!");
        }
        balance -= amount;
        cardBalance.setBalance(balance);
        cardBalanceRepository.save(cardBalance);
    }
}

