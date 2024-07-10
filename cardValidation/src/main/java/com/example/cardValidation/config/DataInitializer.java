package com.example.cardValidation.config;

import com.example.cardValidation.model.Card;
import com.example.cardValidation.model.CardStatus;
import com.example.cardValidation.service.CardService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final CardService cardService;
    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    @Override
    public void run(String... args) throws Exception {
        cardService.deleteAll();

        logger.info("Creating and saving cards...");

        Card card1 = new Card();
        card1.setCardNumber("1234567890123456");
        card1.setPin(1234L);
        card1.setCvc(123L);
        card1.setExpMonth(12L);
        card1.setExpYear(25L);
        card1.setStatus(CardStatus.ACTIVE);

        Card card2 = new Card();
        card2.setCardNumber("6543210987654321");
        card2.setPin(4321L);
        card2.setCvc(321L);
        card2.setExpMonth(11L);
        card2.setExpYear(24L);
        card2.setStatus(CardStatus.BLOCKED);

        Card card3 = new Card();
        card3.setCardNumber("1564148912347896");
        card3.setPin(6543L);
        card3.setCvc(789L);
        card3.setExpMonth(12L);
        card3.setExpYear(26L);
        card3.setStatus(CardStatus.ACTIVE);

        Card card4 = new Card();
        card4.setCardNumber("4567894523453298");
        card4.setPin(7845L);
        card4.setCvc(654L);
        card4.setExpMonth(01L);
        card4.setExpYear(27L);
        card4.setStatus(CardStatus.ACTIVE);

        Card card5 = new Card();
        card5.setCardNumber("9875125635412589");
        card5.setPin(7893L);
        card5.setCvc(459L);
        card5.setExpMonth(06L);
        card5.setExpYear(29L);
        card5.setStatus(CardStatus.ACTIVE);

        cardService.saveCard(card1);
        cardService.saveCard(card2);
        cardService.saveCard(card3);
        cardService.saveCard(card4);
        cardService.saveCard(card5);


        logger.info("Finished creating and saving new cards.");
    }
}