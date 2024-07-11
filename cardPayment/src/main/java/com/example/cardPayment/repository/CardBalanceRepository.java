package com.example.cardPayment.repository;

import com.example.cardPayment.model.CardBalance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CardBalanceRepository extends JpaRepository<CardBalance, Long> {
    public CardBalance findByCardNumber(String cardNumber);
}
