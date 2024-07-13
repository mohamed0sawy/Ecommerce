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

        saveCard("1234123412341234", 1234L, 123L, 12L, 25L, CardStatus.ACTIVE);
        saveCard("5678567856785678", 5678L, 567L, 12L, 25L, CardStatus.BLOCKED);
        saveCard("2345234523452345", 2345L, 234L, 12L, 25L, CardStatus.ACTIVE);
        saveCard("3456345634563456", 3456L, 345L, 12L, 25L, CardStatus.ACTIVE);
        saveCard("4567456745674567", 4567L, 456L, 12L, 25L, CardStatus.ACTIVE);

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
