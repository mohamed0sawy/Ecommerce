package com.example.cardPayment.service;

import com.example.cardPayment.exception.ApiError;
import com.example.cardPayment.model.CardBalance;
import com.example.cardPayment.repository.CardBalanceRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CardBalanceService {

    private final CardBalanceRepository cardBalanceRepository;

    public void saveCardBalance(CardBalance cardBalance) {
        cardBalanceRepository.save(cardBalance);
    }

    public void deleteCardBalances() {
        cardBalanceRepository.deleteAll();
    }

    @Transactional
    public void processPayment(String cardNumber, Long amount) {
        CardBalance cardBalance = cardBalanceRepository.findByCardNumber(cardNumber);
        if(cardBalance == null) {
            throw ApiError.notFound("Card not found with this card number!");
        }
        Long balance = cardBalance.getBalance();
        if(balance < amount) {
            throw ApiError.badRequest("Insufficient funds!");
        }
        balance -= amount;
        cardBalance.setBalance(balance);
        cardBalanceRepository.save(cardBalance);
    }


}
