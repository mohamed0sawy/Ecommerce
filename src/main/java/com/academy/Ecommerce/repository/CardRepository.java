package com.academy.Ecommerce.repository;

import com.academy.Ecommerce.model.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {
    public Card findByCardNumber(String cardNumber);
}
