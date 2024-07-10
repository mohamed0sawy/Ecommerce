package com.example.cardValidation.repository;

import com.example.cardValidation.model.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {
    public Card findByCardNumber(String CardNumber);
}
