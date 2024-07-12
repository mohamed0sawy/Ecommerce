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
    public void run(String... args) {
        cardService.deleteAll();

        logger.info("Creating and saving cards...");

        saveCard("1234567890123456", 1234L, 123L, 12L, 25L, CardStatus.ACTIVE);
        saveCard("6543210987654321", 4321L, 321L, 11L, 24L, CardStatus.BLOCKED);
        saveCard("1564148912347896", 6543L, 789L, 12L, 26L, CardStatus.ACTIVE);
        saveCard("4567894523453298", 7845L, 654L, 1L, 27L, CardStatus.ACTIVE);
        saveCard("9875125635412589", 7893L, 459L, 6L, 29L, CardStatus.ACTIVE);

        logger.info("Finished creating and saving new cards.");
    }

    private void saveCard(String number, Long pin, Long cvc, Long expMonth, Long expYear, CardStatus status) {
        Card card = new Card();
        card.setCardNumber(number);
        card.setPin(pin);
        card.setCvc(cvc);
        card.setExpMonth(expMonth);
        card.setExpYear(expYear);
        card.setStatus(status);
        cardService.saveCard(card);
    }
}
